#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

TARGET_ENV=""
VERSION=""
ARTIFACT_NAME="backend-release"
PUBLISH_MODE="local"
DRY_RUN=0

usage() {
  cat <<'EOF'
Usage: check-env.sh --env <dev|test|staging|prod> --version <version> [--artifact-name <name>] [--publish-mode <local|http|registry>] [--dry-run]
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

TYPE="$(resolve_backend_type)"
[[ -n "${TARGET_ENV}" ]] || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "unknown" "unknown" "Missing required argument: --env"
[[ -n "${VERSION}" ]] || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "unknown" "${TARGET_ENV}" "Missing required argument: --version"
validate_target_env "${TARGET_ENV}" || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Unsupported environment: ${TARGET_ENV}"
validate_version "${VERSION}" || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Invalid version: ${VERSION}"
validate_artifact_name "${ARTIFACT_NAME}" || die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Invalid artifact name: ${ARTIFACT_NAME}"

case "${TYPE}" in
  archive|jar|docker) ;;
  *) die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Unsupported backend artifact type: ${TYPE}" ;;
esac

case "${TYPE}:${PUBLISH_MODE}" in
  archive:local|archive:http|jar:local|jar:http|docker:registry) ;;
  *) die "${EXIT_INVALID_ARGS}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Unsupported publish mode ${PUBLISH_MODE} for artifact type ${TYPE}" ;;
esac

create_runtime_tmpdir
trap cleanup_runtime EXIT
emit_report "check-env.sh" "check-env" "started" "${VERSION}" "${TARGET_ENV}" "Starting backend environment validation"

for cmd in bash cp rm mkdir awk; do
  require_command "${cmd}" || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Required command not found: ${cmd}"
done
[[ -d "${BACKEND_PROJECT_DIR}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Backend project directory not found: ${BACKEND_PROJECT_DIR}"

if [[ "${TYPE}" == "archive" || "${TYPE}" == "jar" ]]; then
  require_command tar || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Required command not found: tar"
  require_command shasum || require_command sha256sum || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Missing checksum command: shasum or sha256sum"
fi

if [[ "${TYPE}" == "docker" ]]; then
  require_command docker || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Required command not found: docker"
  [[ -n "${BACKEND_DOCKERFILE:-}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "BACKEND_DOCKERFILE is required for docker artifact type"
fi

if [[ "${PUBLISH_MODE}" == "http" ]]; then
  require_command curl || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Required command not found: curl"
  require_env_var PUBLISH_BASE_URL || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Missing PUBLISH_BASE_URL"
  require_env_var PUBLISH_UPLOAD_URL || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Missing PUBLISH_UPLOAD_URL"
  require_env_var PUBLISH_AUTH_TOKEN || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Missing PUBLISH_AUTH_TOKEN"
fi

if [[ "${PUBLISH_MODE}" == "registry" ]]; then
  require_command docker || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "Required command not found: docker"
  [[ -n "${DOCKER_REGISTRY:-}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "DOCKER_REGISTRY is required for registry publish"
fi

if contains_action register; then
  [[ -n "${REGISTER_CMD:-}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "REGISTER_CMD is required when RELEASE_ACTIONS contains register"
fi
if contains_action deploy; then
  [[ -n "${DEPLOY_CMD:-}" ]] || die "${EXIT_CHECK_FAILED}" "check-env.sh" "check-env" "${VERSION}" "${TARGET_ENV}" "DEPLOY_CMD is required when RELEASE_ACTIONS contains deploy"
fi

emit_report "check-env.sh" "check-env" "succeeded" "${VERSION}" "${TARGET_ENV}" "Backend environment validation passed"
