#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

TARGET_ENV=""
VERSION=""
ARTIFACT_NAME="frontend-dist"
PUBLISH_MODE="local"
CONFIRM_PROD=0
DRY_RUN=0

usage() {
  cat <<'EOF'
Usage: publish.sh --env <dev|test|staging|prod> --version <version> [--artifact-name <name>] [--publish-mode <local|http>] [--confirm-prod] [--dry-run]

Publishes frontend artifacts only. It does not rebuild artifacts.
EOF
  print_help_footer
}

remote_file_exists() {
  local url="$1"
  local http_code
  http_code="$(curl --silent --output /dev/null --write-out '%{http_code}' -H "Authorization: Bearer ${PUBLISH_AUTH_TOKEN}" "${url}")"
  [[ "${http_code}" == "200" ]]
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --env) TARGET_ENV="${2:-}"; shift 2 ;;
    --version) VERSION="${2:-}"; shift 2 ;;
    --artifact-name) ARTIFACT_NAME="${2:-}"; shift 2 ;;
    --publish-mode) PUBLISH_MODE="${2:-}"; shift 2 ;;
    --confirm-prod) CONFIRM_PROD=1; shift ;;
    --dry-run) DRY_RUN=1; shift ;;
    --help) usage; exit 0 ;;
    *) usage >&2; exit "${EXIT_INVALID_ARGS}" ;;
  esac
done

[[ -n "${TARGET_ENV}" ]] || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "unknown" "unknown" "Missing required argument: --env"
[[ -n "${VERSION}" ]] || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "unknown" "${TARGET_ENV}" "Missing required argument: --version"
validate_target_env "${TARGET_ENV}" || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Unsupported environment: ${TARGET_ENV}"
validate_version "${VERSION}" || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Invalid version: ${VERSION}"
validate_artifact_name "${ARTIFACT_NAME}" || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Invalid artifact name: ${ARTIFACT_NAME}"

case "${PUBLISH_MODE}" in
  local|http) ;;
  *) die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Unsupported publish mode: ${PUBLISH_MODE}" ;;
esac

if is_prod_env "${TARGET_ENV}" && [[ "${CONFIRM_PROD}" != "1" ]]; then
  die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Production publish requires --confirm-prod"
fi

create_runtime_tmpdir
trap cleanup_runtime EXIT
emit_report "publish.sh" "publish" "started" "${VERSION}" "${TARGET_ENV}" "Starting frontend publish"

ARCHIVE_PATH="$(artifact_archive_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
CHECKSUM_PATH="$(artifact_checksum_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
MANIFEST_PATH="$(artifact_manifest_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
[[ -f "${ARCHIVE_PATH}" ]] || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Artifact archive not found: ${ARCHIVE_PATH}"

if contains_action publish; then
  if [[ "${PUBLISH_MODE}" == "local" ]]; then
    : "${PUBLISH_TARGET_DIR:=${PUBLISHED_DIR}/${TARGET_ENV}}"
    if [[ "${DRY_RUN}" == "1" ]]; then
      log_info "[dry-run] mkdir -p ${PUBLISH_TARGET_DIR}"
      log_info "[dry-run] cp ${ARCHIVE_PATH} ${PUBLISH_TARGET_DIR}/$(basename "${ARCHIVE_PATH}")"
      log_info "[dry-run] cp ${CHECKSUM_PATH} ${PUBLISH_TARGET_DIR}/$(basename "${CHECKSUM_PATH}")"
      log_info "[dry-run] cp ${MANIFEST_PATH} ${PUBLISH_TARGET_DIR}/$(basename "${MANIFEST_PATH}")"
    else
      ensure_dir "${PUBLISH_TARGET_DIR}"
      cp "${ARCHIVE_PATH}" "${PUBLISH_TARGET_DIR}/$(basename "${ARCHIVE_PATH}")"
      cp "${CHECKSUM_PATH}" "${PUBLISH_TARGET_DIR}/$(basename "${CHECKSUM_PATH}")"
      cp "${MANIFEST_PATH}" "${PUBLISH_TARGET_DIR}/$(basename "${MANIFEST_PATH}")"
    fi
  else
    require_command curl || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Required command not found: curl"
    REMOTE_UPLOAD_BASE="${PUBLISH_UPLOAD_URL%/}/${TARGET_ENV}"
    REMOTE_ARCHIVE_URL="${PUBLISH_BASE_URL%/}/${TARGET_ENV}/$(basename "${ARCHIVE_PATH}")"
    if [[ "${DRY_RUN}" == "1" ]]; then
      log_info "[dry-run] curl --upload-file ${ARCHIVE_PATH} ${REMOTE_UPLOAD_BASE}/$(basename "${ARCHIVE_PATH}")"
      log_info "[dry-run] curl --upload-file ${CHECKSUM_PATH} ${REMOTE_UPLOAD_BASE}/$(basename "${CHECKSUM_PATH}")"
      log_info "[dry-run] curl --upload-file ${MANIFEST_PATH} ${REMOTE_UPLOAD_BASE}/$(basename "${MANIFEST_PATH}")"
    else
      if remote_file_exists "${REMOTE_ARCHIVE_URL}"; then
        die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Remote artifact already exists: ${REMOTE_ARCHIVE_URL}"
      fi
      curl --fail --silent -H "Authorization: Bearer ${PUBLISH_AUTH_TOKEN}" --upload-file "${ARCHIVE_PATH}" "${REMOTE_UPLOAD_BASE}/$(basename "${ARCHIVE_PATH}")" >/dev/null || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Artifact upload failed"
      curl --fail --silent -H "Authorization: Bearer ${PUBLISH_AUTH_TOKEN}" --upload-file "${CHECKSUM_PATH}" "${REMOTE_UPLOAD_BASE}/$(basename "${CHECKSUM_PATH}")" >/dev/null || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Checksum upload failed"
      curl --fail --silent -H "Authorization: Bearer ${PUBLISH_AUTH_TOKEN}" --upload-file "${MANIFEST_PATH}" "${REMOTE_UPLOAD_BASE}/$(basename "${MANIFEST_PATH}")" >/dev/null || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Manifest upload failed"
    fi
  fi
fi

if contains_action register; then
  if [[ "${DRY_RUN}" == "1" ]]; then
    log_info "[dry-run] ${REGISTER_CMD}"
  else
    bash -lc "${REGISTER_CMD}" || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Register action failed"
  fi
fi

if contains_action deploy; then
  if [[ "${DRY_RUN}" == "1" ]]; then
    log_info "[dry-run] ${DEPLOY_CMD}"
  else
    bash -lc "${DEPLOY_CMD}" || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Deploy action failed"
  fi
fi

emit_report "publish.sh" "publish" "succeeded" "${VERSION}" "${TARGET_ENV}" "Frontend publish completed"
