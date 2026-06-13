#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

TARGET_ENV=""
VERSION=""
ARTIFACT_NAME="frontend-dist"
INSTALL_DEPS="auto"
SKIP_TESTS=0
DRY_RUN=0

usage() {
  cat <<'EOF'
Usage: build-base.sh --env <dev|test|staging|prod> --version <version> [--artifact-name <name>] [--install-deps <auto|always|never>] [--skip-tests] [--dry-run]

Builds frontend artifacts only. It does not publish.
EOF
  print_help_footer
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --env) TARGET_ENV="${2:-}"; shift 2 ;;
    --version) VERSION="${2:-}"; shift 2 ;;
    --artifact-name) ARTIFACT_NAME="${2:-}"; shift 2 ;;
    --install-deps) INSTALL_DEPS="${2:-}"; shift 2 ;;
    --skip-tests) SKIP_TESTS=1; shift ;;
    --dry-run) DRY_RUN=1; shift ;;
    --help) usage; exit 0 ;;
    *) usage >&2; exit "${EXIT_INVALID_ARGS}" ;;
  esac
done

[[ -n "${TARGET_ENV}" ]] || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "unknown" "unknown" "Missing required argument: --env"
[[ -n "${VERSION}" ]] || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "unknown" "${TARGET_ENV}" "Missing required argument: --version"
validate_target_env "${TARGET_ENV}" || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Unsupported environment: ${TARGET_ENV}"
validate_version "${VERSION}" || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Invalid version: ${VERSION}"

case "${INSTALL_DEPS}" in
  auto|always|never) ;;
  *) die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Unsupported dependency mode: ${INSTALL_DEPS}" ;;
esac

create_runtime_tmpdir
trap cleanup_runtime EXIT
emit_report "build-base.sh" "build-base" "started" "${VERSION}" "${TARGET_ENV}" "Starting frontend build"

BOOTSTRAP_CMD="$(resolve_bootstrap_command)"
VALIDATE_CMD="$(resolve_validate_command)"
BUILD_CMD="$(resolve_build_command)"
OUTPUT_DIR="$(resolve_frontend_output_dir)"

require_command bash || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Required command not found: bash"
require_command tar || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Required command not found: tar"

ensure_dir "${ARTIFACTS_DIR}"

if [[ "${INSTALL_DEPS}" == "always" || ( "${INSTALL_DEPS}" == "auto" && -n "${FRONTEND_BOOTSTRAP_CHECK_CMD:-}" ) ]]; then
  if [[ "${INSTALL_DEPS}" != "always" && "${DRY_RUN}" != "1" ]]; then
    bash -lc "cd \"${FRONTEND_PROJECT_DIR}\" && ${FRONTEND_BOOTSTRAP_CHECK_CMD}" >/dev/null 2>&1 || INSTALL_DEPS="always"
  fi
fi

if [[ "${INSTALL_DEPS}" == "always" ]]; then
  if [[ "${DRY_RUN}" == "1" ]]; then
    log_info "[dry-run] ${BOOTSTRAP_CMD}"
  else
    ( cd "${FRONTEND_PROJECT_DIR}" && bash -lc "${BOOTSTRAP_CMD}" ) || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Dependency installation failed"
  fi
fi

if [[ "${SKIP_TESTS}" == "0" ]]; then
  if [[ "${DRY_RUN}" == "1" ]]; then
    log_info "[dry-run] ${VALIDATE_CMD}"
  else
    ( cd "${FRONTEND_PROJECT_DIR}" && bash -lc "${VALIDATE_CMD}" ) || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Validation failed"
  fi
fi

if [[ "${DRY_RUN}" == "1" ]]; then
  log_info "[dry-run] ${BUILD_CMD}"
else
  ( cd "${FRONTEND_PROJECT_DIR}" && bash -lc "${BUILD_CMD}" ) || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Build command failed"
fi

[[ -d "${OUTPUT_DIR}" || "${DRY_RUN}" == "1" ]] || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Build output directory not found: ${OUTPUT_DIR}"

ARCHIVE_PATH="$(artifact_archive_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
CHECKSUM_PATH="$(artifact_checksum_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
MANIFEST_PATH="$(artifact_manifest_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"

if [[ "${DRY_RUN}" == "1" ]]; then
  log_info "[dry-run] tar -C ${OUTPUT_DIR} -czf ${ARCHIVE_PATH} ."
else
  rm -f "${ARCHIVE_PATH}" "${CHECKSUM_PATH}" "${MANIFEST_PATH}"
  tar -C "${OUTPUT_DIR}" -czf "${ARCHIVE_PATH}" . || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Artifact archive creation failed"
  ARTIFACT_SHA256="$(compute_sha256 "${ARCHIVE_PATH}")" || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Checksum generation failed"
  printf '%s  %s\n' "${ARTIFACT_SHA256}" "$(basename "${ARCHIVE_PATH}")" > "${CHECKSUM_PATH}"
  cat > "${MANIFEST_PATH}" <<EOF
{
  "component": "frontend",
  "artifact_name": "${ARTIFACT_NAME}",
  "environment": "${TARGET_ENV}",
  "version": "${VERSION}",
  "project_dir": "${FRONTEND_PROJECT_DIR}",
  "output_dir": "${OUTPUT_DIR}",
  "build_command": "${BUILD_CMD}",
  "created_at": "$(timestamp_utc)",
  "checksum_sha256": "${ARTIFACT_SHA256}"
}
EOF
fi

emit_report "build-base.sh" "build-base" "succeeded" "${VERSION}" "${TARGET_ENV}" "Frontend artifact created"
