#!/usr/bin/env bash

set -euo pipefail

readonly EXIT_OK=0
readonly EXIT_INVALID_ARGS=2
readonly EXIT_CHECK_FAILED=3
readonly EXIT_BUILD_FAILED=4
readonly EXIT_PUBLISH_FAILED=5
readonly EXIT_RUNTIME_FAILED=6

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
BACKEND_PROJECT_DIR="${BACKEND_PROJECT_DIR:-${PROJECT_ROOT}}"
ARTIFACTS_DIR="${SCRIPT_DIR}/artifacts"
PUBLISHED_DIR="${SCRIPT_DIR}/published"
TMP_ROOT="${SCRIPT_DIR}/tmp"
REPORT_SCRIPT="${SCRIPT_DIR}/report.sh"
RUNTIME_TMP_DIR=""

timestamp_utc() { date -u "+%Y-%m-%dT%H:%M:%SZ"; }
log_info() { printf '[INFO] %s %s\n' "$(timestamp_utc)" "$*"; }
log_warn() { printf '[WARN] %s %s\n' "$(timestamp_utc)" "$*"; }
log_error() { printf '[ERROR] %s %s\n' "$(timestamp_utc)" "$*" >&2; }

json_escape() {
  local value="${1:-}"
  value="${value//\\/\\\\}"
  value="${value//\"/\\\"}"
  value="${value//$'\n'/\\n}"
  value="${value//$'\r'/\\r}"
  value="${value//$'\t'/\\t}"
  printf '%s' "${value}"
}

emit_report() {
  if [[ -x "${REPORT_SCRIPT}" ]]; then
    "${REPORT_SCRIPT}" --script-name "$1" --stage "$2" --status "$3" --version "$4" --env "$5" --message "${6:-}"
  fi
}

die() {
  local exit_code="$1"
  local script_name="$2"
  local stage_name="$3"
  local version="$4"
  local target_env="$5"
  shift 5
  local message="$*"
  log_error "${message}"
  emit_report "${script_name}" "${stage_name}" "failed" "${version}" "${target_env}" "${message}"
  exit "${exit_code}"
}

ensure_dir() { mkdir -p "$1"; }
create_runtime_tmpdir() { ensure_dir "${TMP_ROOT}"; RUNTIME_TMP_DIR="$(mktemp -d "${TMP_ROOT}/run.XXXXXX")"; }
cleanup_runtime() { [[ -n "${RUNTIME_TMP_DIR}" && -d "${RUNTIME_TMP_DIR}" ]] && rm -rf "${RUNTIME_TMP_DIR}"; }
require_command() { command -v "$1" >/dev/null 2>&1 || return 1; }
require_env_var() { local env_name="$1"; [[ -n "${!env_name:-}" ]]; }
validate_target_env() { case "$1" in dev|test|staging|prod) return 0 ;; *) return 1 ;; esac; }
validate_version() { [[ "$1" =~ ^[0-9A-Za-z][0-9A-Za-z._-]*$ ]]; }
validate_artifact_name() { [[ "$1" =~ ^[0-9A-Za-z][0-9A-Za-z._-]*$ ]]; }
is_prod_env() { [[ "$1" == "prod" ]]; }
contains_action() { local actions=",${RELEASE_ACTIONS:-publish},"; [[ "${actions}" == *",$1,"* ]]; }

compute_sha256() {
  local file_path="$1"
  if command -v sha256sum >/dev/null 2>&1; then
    sha256sum "${file_path}" | awk '{print $1}'
    return 0
  fi
  if command -v shasum >/dev/null 2>&1; then
    shasum -a 256 "${file_path}" | awk '{print $1}'
    return 0
  fi
  return 1
}

resolve_backend_type() { printf '%s' "${BACKEND_ARTIFACT_TYPE:-archive}"; }
resolve_bootstrap_command() { printf '%s' "${BACKEND_BOOTSTRAP_CMD:-}"; }
resolve_validate_command() { printf '%s' "${BACKEND_VALIDATE_CMD:-}"; }
resolve_build_command() { printf '%s' "${BACKEND_BUILD_CMD:-}"; }
resolve_output_path() { printf '%s' "${BACKEND_OUTPUT_PATH:-}"; }
resolve_image_name() { printf '%s' "${BACKEND_IMAGE_NAME:-backend-app}"; }
resolve_image_tag() { printf '%s' "${BACKEND_IMAGE_TAG:-latest}"; }

artifact_basename() { printf '%s-%s-%s' "$1" "$2" "$3"; }
artifact_archive_path() { printf '%s/%s.tar.gz' "${ARTIFACTS_DIR}" "$(artifact_basename "$1" "$2" "$3")"; }
artifact_file_path() { printf '%s/%s.bin' "${ARTIFACTS_DIR}" "$(artifact_basename "$1" "$2" "$3")"; }
artifact_checksum_path() { printf '%s/%s.sha256' "${ARTIFACTS_DIR}" "$(artifact_basename "$1" "$2" "$3")"; }
artifact_manifest_path() { printf '%s/%s.manifest.json' "${ARTIFACTS_DIR}" "$(artifact_basename "$1" "$2" "$3")"; }

print_help_footer() {
  cat <<'EOF'
Options:
  --help       Show this help message.
  --dry-run    Print commands without executing them.
EOF
}
