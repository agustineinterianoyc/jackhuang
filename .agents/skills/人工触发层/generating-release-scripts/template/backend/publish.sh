#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

TARGET_ENV=""
VERSION=""
ARTIFACT_NAME="backend-release"
PUBLISH_MODE="local"
CONFIRM_PROD=0
DRY_RUN=0

usage() {
  cat <<'EOF'
Usage: publish.sh --env <dev|test|staging|prod> --version <version> [--artifact-name <name>] [--publish-mode <local|http|registry>] [--confirm-prod] [--dry-run]

Publishes backend artifacts only. It does not rebuild artifacts.
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

TYPE="$(resolve_backend_type)"
[[ -n "${TARGET_ENV}" ]] || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "unknown" "unknown" "Missing required argument: --env"
[[ -n "${VERSION}" ]] || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "unknown" "${TARGET_ENV}" "Missing required argument: --version"
validate_target_env "${TARGET_ENV}" || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Unsupported environment: ${TARGET_ENV}"
validate_version "${VERSION}" || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Invalid version: ${VERSION}"
validate_artifact_name "${ARTIFACT_NAME}" || die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Invalid artifact name: ${ARTIFACT_NAME}"

case "${TYPE}:${PUBLISH_MODE}" in
  archive:local|archive:http|jar:local|jar:http|docker:registry) ;;
  *) die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Unsupported publish mode ${PUBLISH_MODE} for artifact type ${TYPE}" ;;
esac

if is_prod_env "${TARGET_ENV}" && [[ "${CONFIRM_PROD}" != "1" ]]; then
  die "${EXIT_INVALID_ARGS}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Production publish requires --confirm-prod"
fi

create_runtime_tmpdir
trap cleanup_runtime EXIT
emit_report "publish.sh" "publish" "started" "${VERSION}" "${TARGET_ENV}" "Starting backend publish"

case "${TYPE}" in
  archive)
    ARCHIVE_PATH="$(artifact_archive_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    CHECKSUM_PATH="$(artifact_checksum_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    MANIFEST_PATH="$(artifact_manifest_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    [[ -f "${ARCHIVE_PATH}" ]] || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Archive artifact not found"
    if contains_action publish; then
      if [[ "${PUBLISH_MODE}" == "local" ]]; then
        : "${PUBLISH_TARGET_DIR:=${PUBLISHED_DIR}/${TARGET_ENV}}"
        if [[ "${DRY_RUN}" == "1" ]]; then
          log_info "[dry-run] mkdir -p ${PUBLISH_TARGET_DIR}"
          log_info "[dry-run] cp ${ARCHIVE_PATH} ${PUBLISH_TARGET_DIR}/$(basename "${ARCHIVE_PATH}")"
        else
          ensure_dir "${PUBLISH_TARGET_DIR}"
          cp "${ARCHIVE_PATH}" "${PUBLISH_TARGET_DIR}/$(basename "${ARCHIVE_PATH}")"
          cp "${CHECKSUM_PATH}" "${PUBLISH_TARGET_DIR}/$(basename "${CHECKSUM_PATH}")"
          cp "${MANIFEST_PATH}" "${PUBLISH_TARGET_DIR}/$(basename "${MANIFEST_PATH}")"
        fi
      else
        REMOTE_UPLOAD_BASE="${PUBLISH_UPLOAD_URL%/}/${TARGET_ENV}"
        if [[ "${DRY_RUN}" == "1" ]]; then
          log_info "[dry-run] curl --upload-file ${ARCHIVE_PATH} ${REMOTE_UPLOAD_BASE}/$(basename "${ARCHIVE_PATH}")"
        else
          curl --fail --silent -H "Authorization: Bearer ${PUBLISH_AUTH_TOKEN}" --upload-file "${ARCHIVE_PATH}" "${REMOTE_UPLOAD_BASE}/$(basename "${ARCHIVE_PATH}")" >/dev/null || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Archive upload failed"
        fi
      fi
    fi
    ;;
  jar)
    FILE_PATH="$(artifact_file_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    CHECKSUM_PATH="$(artifact_checksum_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    MANIFEST_PATH="$(artifact_manifest_path "${ARTIFACT_NAME}" "${VERSION}" "${TARGET_ENV}")"
    [[ -f "${FILE_PATH}" ]] || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Jar/War artifact not found"
    if contains_action publish; then
      if [[ "${PUBLISH_MODE}" == "local" ]]; then
        : "${PUBLISH_TARGET_DIR:=${PUBLISHED_DIR}/${TARGET_ENV}}"
        if [[ "${DRY_RUN}" == "1" ]]; then
          log_info "[dry-run] mkdir -p ${PUBLISH_TARGET_DIR}"
          log_info "[dry-run] cp ${FILE_PATH} ${PUBLISH_TARGET_DIR}/$(basename "${FILE_PATH}")"
        else
          ensure_dir "${PUBLISH_TARGET_DIR}"
          cp "${FILE_PATH}" "${PUBLISH_TARGET_DIR}/$(basename "${FILE_PATH}")"
          cp "${CHECKSUM_PATH}" "${PUBLISH_TARGET_DIR}/$(basename "${CHECKSUM_PATH}")"
          cp "${MANIFEST_PATH}" "${PUBLISH_TARGET_DIR}/$(basename "${MANIFEST_PATH}")"
        fi
      else
        REMOTE_UPLOAD_BASE="${PUBLISH_UPLOAD_URL%/}/${TARGET_ENV}"
        if [[ "${DRY_RUN}" == "1" ]]; then
          log_info "[dry-run] curl --upload-file ${FILE_PATH} ${REMOTE_UPLOAD_BASE}/$(basename "${FILE_PATH}")"
        else
          curl --fail --silent -H "Authorization: Bearer ${PUBLISH_AUTH_TOKEN}" --upload-file "${FILE_PATH}" "${REMOTE_UPLOAD_BASE}/$(basename "${FILE_PATH}")" >/dev/null || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Jar/War upload failed"
        fi
      fi
    fi
    ;;
  docker)
    IMAGE_NAME="$(resolve_image_name)"
    IMAGE_TAG="$(resolve_image_tag)"
    TARGET_IMAGE="${DOCKER_REGISTRY%/}/${IMAGE_NAME}:${IMAGE_TAG}"
    if contains_action publish; then
      if [[ "${DRY_RUN}" == "1" ]]; then
        log_info "[dry-run] docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${TARGET_IMAGE}"
        log_info "[dry-run] docker push ${TARGET_IMAGE}"
      else
        docker tag "${IMAGE_NAME}:${IMAGE_TAG}" "${TARGET_IMAGE}" || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Docker tag failed"
        docker push "${TARGET_IMAGE}" || die "${EXIT_PUBLISH_FAILED}" "publish.sh" "publish" "${VERSION}" "${TARGET_ENV}" "Docker push failed"
      fi
    fi
    ;;
esac

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

emit_report "publish.sh" "publish" "succeeded" "${VERSION}" "${TARGET_ENV}" "Backend publish completed"
