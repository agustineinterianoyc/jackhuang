#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

TARGET_ENV=""
VERSION=""
ARTIFACT_NAME="frontend-dist"
PUBLISH_MODE="local"
DRY_RUN=0

usage() {
  cat <<'EOF'
Usage: check-env.sh --env <dev|test|staging|prod> --version <version> [--artifact-name <name>] [--publish-mode <local|http>] [--dry-run]

Checks frontend release environment only. It does not build or publish.
EOF
  print_help_footer
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --env) TARGET_ENV="${2:-}"; shift 2 ;;
    --version) VERSION="${2:-}"; shift 2 ;;
    --artifact-name) ARTIFACT_NAME="${2:-}"; shift 2 ;;
    --publish-mode) PUBLISH_MODE="${2:-}"; shift 2 ;;
    --dry-run) DRY_RUN=1; shift ;;
    --help) usage; exit 0 ;;
    *) usage >&2; exit "${EXIT_INVALID_ARGS}" ;;
  esac
done

[[ -n "${TARGET_ENV}" ]] || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "unknown" "unknown" "Missing required argument: --env"
[[ -n "${VERSION}" ]] || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "unknown" "${TARGET_ENV}" "Missing required argument: --version"
validate_target_env "${TARGET_ENV}" || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Unsupported environment: ${TARGET_ENV}"
validate_version "${VERSION}" || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Invalid version: ${VERSION}"
validate_artifact_name "${ARTIFACT_NAME}" || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Invalid artifact name: ${ARTIFACT_NAME}"

case "${PUBLISH_MODE}" in
  local|http) ;;
  *) die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Unsupported publish mode: ${PUBLISH_MODE}" ;;
esac

create_runtime_tmpdir
trap cleanup_runtime EXIT

emit_report "check-env.sh" "check-env" "started" "${VERSION}" "${TARGET_ENV}" "Starting frontend environment validation"

for cmd in bash tar cp rm mkdir awk; do
  require_command "${cmd}" || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Required command not found: ${cmd}"
done
require_command shasum || require_command sha256sum || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Missing checksum command: shasum or sha256sum"
if [[ "${PUBLISH_MODE}" == "http" ]]; then
  require_command curl || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Required command not found: curl"
fi

ensure_dir "${ARTIFACTS_DIR}"
ensure_dir "${PUBLISHED_DIR}"
ensure_dir "${TMP_ROOT}"
[[ -d "${FRONTEND_PROJECT_DIR}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Frontend project directory not found: ${FRONTEND_PROJECT_DIR}"
[[ -w "${ARTIFACTS_DIR}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Artifacts directory is not writable: ${ARTIFACTS_DIR}"

if [[ "${PUBLISH_MODE}" == "local" ]]; then
  : "${PUBLISH_TARGET_DIR:=${PUBLISHED_DIR}/${TARGET_ENV}}"
  ensure_dir "${PUBLISH_TARGET_DIR}"
  [[ -w "${PUBLISH_TARGET_DIR}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Local publish target is not writable: ${PUBLISH_TARGET_DIR}"
else
  require_env_var PUBLISH_BASE_URL || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Missing required environment variable: PUBLISH_BASE_URL"
  require_env_var PUBLISH_UPLOAD_URL || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Missing required environment variable: PUBLISH_UPLOAD_URL"
  require_env_var PUBLISH_AUTH_TOKEN || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Missing required credential: PUBLISH_AUTH_TOKEN"
fi

if is_prod_env "${TARGET_ENV}"; then
  require_env_var RELEASE_APPROVER || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Missing required environment variable for prod: RELEASE_APPROVER"
fi

if contains_action register; then
  [[ -n "${REGISTER_CMD:-}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "REGISTER_CMD is required when RELEASE_ACTIONS contains register"
fi
if contains_action deploy; then
  [[ -n "${DEPLOY_CMD:-}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "DEPLOY_CMD is required when RELEASE_ACTIONS contains deploy"
fi

emit_report "check-env.sh" "check-env" "succeeded" "${VERSION}" "${TARGET_ENV}" "Frontend environment validation passed"
