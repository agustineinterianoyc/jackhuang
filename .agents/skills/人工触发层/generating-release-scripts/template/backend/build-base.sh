#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

TARGET_ENV=""
VERSION=""
ARTIFACT_NAME="backend-release"
SKIP_TESTS=0
DRY_RUN=0

usage() {
  cat <<'EOF'
Usage: build-base.sh --env <dev|test|staging|prod> --version <version> [--artifact-name <name>] [--skip-tests] [--dry-run]

Builds backend artifacts only. It does not publish.
EOF
  print_help_footer
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --env) TARGET_ENV="${2:-}"; shift 2 ;;
    --version) VERSION="${2:-}"; shift 2 ;;
    --artifact-name) ARTIFACT_NAME="${2:-}"; shift 2 ;;
    --skip-tests) SKIP_TESTS=1; shift ;;
    --dry-run) DRY_RUN=1; shift ;;
    --help) usage; exit 0 ;;
    *) usage >&2; exit "${EXIT_INVALID_ARGS}" ;;
  esac
done

TYPE="$(resolve_backend_type)"
BOOTSTRAP_CMD="$(resolve_bootstrap_command)"
VALIDATE_CMD="$(resolve_validate_command)"
BUILD_CMD="$(resolve_build_command)"
OUTPUT_PATH="$(resolve_output_path)"

[[ -n "${TARGET_ENV}" ]] || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "unknown" "unknown" "Missing required argument: --env"
[[ -n "${VERSION}" ]] || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "unknown" "${TARGET_ENV}" "Missing required argument: --version"
validate_target_env "${TARGET_ENV}" || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Unsupported environment: ${TARGET_ENV}"
validate_version "${VERSION}" || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Invalid version: ${VERSION}"
validate_artifact_name "${ARTIFACT_NAME}" || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Invalid artifact name: ${ARTIFACT_NAME}"

case "${TYPE}" in
  archive|jar|docker) ;;
  *) die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Unsupported backend artifact type: ${TYPE}" ;;
esac

[[ -n "${OUTPUT_PATH}" || "${TYPE}" == "docker" ]] || die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "BACKEND_OUTPUT_PATH is required for artifact type ${TYPE}"

create_runtime_tmpdir
trap cleanup_runtime EXIT
emit_report "build-base.sh" "build-base" "started" "${VERSION}" "${TARGET_ENV}" "Starting backend build"
ensure_dir "${ARTIFACTS_DIR}"

if [[ -n "${BOOTSTRAP_CMD}" ]]; then
  if [[ "${DRY_RUN}" == "1" ]]; then
    log_info "[dry-run] ${BOOTSTRAP_CMD}"
  else
    ( cd "${BACKEND_PROJECT_DIR}" && bash -lc "${BOOTSTRAP_CMD}" ) || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Bootstrap failed"
  fi
fi

if [[ "${SKIP_TESTS}" == "0" && -n "${VALIDATE_CMD}" ]]; then
  if [[ "${DRY_RUN}" == "1" ]]; then
    log_info "[dry-run] ${VALIDATE_CMD}"
  else
    ( cd "${BACKEND_PROJECT_DIR}" && bash -lc "${VALIDATE_CMD}" ) || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Validation failed"
  fi
fi

if [[ -n "${BUILD_CMD}" ]]; then
  if [[ "${DRY_RUN}" == "1" ]]; then
    log_info "[dry-run] ${BUILD_CMD}"
  else
    ( cd "${BACKEND_PROJECT_DIR}" && bash -lc "${BUILD_CMD}" ) || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Build failed"
  fi
fi

MANIFEST_PATH="$(artifact_manifest_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"

case "${TYPE}" in
  archive)
    [[ -d "${OUTPUT_PATH}" || "${DRY_RUN}" == "1" ]] || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Output directory not found: ${OUTPUT_PATH}"
    ARCHIVE_PATH="$(artifact_archive_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    CHECKSUM_PATH="$(artifact_checksum_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    if [[ "${DRY_RUN}" != "1" ]]; then
      tar -C "${OUTPUT_PATH}" -czf "${ARCHIVE_PATH}" . || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Archive packaging failed"
      ARTIFACT_SHA256="$(compute_sha256 "${ARCHIVE_PATH}")" || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Checksum generation failed"
      printf '%s  %s\n' "${ARTIFACT_SHA256}" "$(basename "${ARCHIVE_PATH}")" > "${CHECKSUM_PATH}"
    fi
    ;;
  jar)
    [[ -f "${OUTPUT_PATH}" || "${DRY_RUN}" == "1" ]] || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Jar/War file not found: ${OUTPUT_PATH}"
    FILE_PATH="$(artifact_file_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    CHECKSUM_PATH="$(artifact_checksum_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    if [[ "${DRY_RUN}" != "1" ]]; then
      cp "${OUTPUT_PATH}" "${FILE_PATH}"
      ARTIFACT_SHA256="$(compute_sha256 "${FILE_PATH}")" || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Checksum generation failed"
      printf '%s  %s\n' "${ARTIFACT_SHA256}" "$(basename "${FILE_PATH}")" > "${CHECKSUM_PATH}"
    fi
    ;;
  docker)
    IMAGE_NAME="$(resolve_image_name)"
    IMAGE_TAG="$(resolve_image_tag)"
    if [[ "${DRY_RUN}" == "1" ]]; then
      log_info "[dry-run] docker build -f ${BACKEND_DOCKERFILE} -t ${IMAGE_NAME}:${IMAGE_TAG} ${BACKEND_PROJECT_DIR}"
    else
      docker build -f "${BACKEND_DOCKERFILE}" -t "${IMAGE_NAME}:${IMAGE_TAG}" "${BACKEND_PROJECT_DIR}" || die "${EXIT_BUILD_FAILED}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Docker build failed"
    fi
    ;;
  *) die "${EXIT_INVALID_ARGS}" "build-base.sh" "build-base" "${VERSION}" "${TARGET_ENV}" "Unsupported backend artifact type: ${TYPE}" ;;
esac

if [[ "${DRY_RUN}" != "1" ]]; then
  cat > "${MANIFEST_PATH}" <<EOF
{
  "component": "backend",
  "artifact_type": "${TYPE}",
  "artifact_name": "${ARTIFACT_NAME}",
  "environment": "${TARGET_ENV}",
  "version": "${VERSION}",
  "project_dir": "${BACKEND_PROJECT_DIR}",
  "output_path": "${OUTPUT_PATH}",
  "build_command": "${BUILD_CMD}",
  "created_at": "$(timestamp_utc)"
}
EOF
fi

emit_report "build-base.sh" "build-base" "succeeded" "${VERSION}" "${TARGET_ENV}" "Backend build completed"
