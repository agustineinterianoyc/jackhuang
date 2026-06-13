#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FRONTEND_SCRIPT_DIR="${FRONTEND_SCRIPT_DIR:-${SCRIPT_DIR}/../frontend}"
BACKEND_SCRIPT_DIR="${BACKEND_SCRIPT_DIR:-${SCRIPT_DIR}/../backend}"

TARGET_ENV=""
VERSION=""
RUN_FRONTEND=1
RUN_BACKEND=1
FRONTEND_PUBLISH_MODE="local"
BACKEND_PUBLISH_MODE="local"
CONFIRM_PROD=0
DRY_RUN=0

usage() {
  cat <<'EOF'
Usage: orchestrate.sh --env <dev|test|staging|prod> --version <version> [--frontend-only] [--backend-only] [--frontend-publish-mode <mode>] [--backend-publish-mode <mode>] [--confirm-prod] [--dry-run]

Orchestrates frontend and backend release scripts.
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --env) TARGET_ENV="${2:-}"; shift 2 ;;
    --version) VERSION="${2:-}"; shift 2 ;;
    --frontend-only) RUN_BACKEND=0; shift ;;
    --backend-only) RUN_FRONTEND=0; shift ;;
    --frontend-publish-mode) FRONTEND_PUBLISH_MODE="${2:-}"; shift 2 ;;
    --backend-publish-mode) BACKEND_PUBLISH_MODE="${2:-}"; shift 2 ;;
    --confirm-prod) CONFIRM_PROD=1; shift ;;
    --dry-run) DRY_RUN=1; shift ;;
    --help) usage; exit 0 ;;
    *) usage >&2; exit 2 ;;
  esac
done

[[ -n "${TARGET_ENV}" && -n "${VERSION}" ]] || { usage >&2; exit 2; }
if [[ "${RUN_FRONTEND}" != "1" && "${RUN_BACKEND}" != "1" ]]; then
  usage >&2
  exit 2
fi

COMMON_ARGS=(--env "${TARGET_ENV}" --version "${VERSION}")
PUBLISH_ARGS=()
if [[ "${CONFIRM_PROD}" == "1" ]]; then
  PUBLISH_ARGS+=(--confirm-prod)
fi
if [[ "${DRY_RUN}" == "1" ]]; then
  COMMON_ARGS+=(--dry-run)
fi

if [[ "${RUN_FRONTEND}" == "1" ]]; then
  "${FRONTEND_SCRIPT_DIR}/check-env.sh" "${COMMON_ARGS[@]}" --publish-mode "${FRONTEND_PUBLISH_MODE}"
  "${FRONTEND_SCRIPT_DIR}/build-base.sh" "${COMMON_ARGS[@]}"
  "${FRONTEND_SCRIPT_DIR}/publish.sh" "${COMMON_ARGS[@]}" --publish-mode "${FRONTEND_PUBLISH_MODE}" "${PUBLISH_ARGS[@]}"
fi

if [[ "${RUN_BACKEND}" == "1" ]]; then
  "${BACKEND_SCRIPT_DIR}/check-env.sh" "${COMMON_ARGS[@]}" --publish-mode "${BACKEND_PUBLISH_MODE}"
  "${BACKEND_SCRIPT_DIR}/build-base.sh" "${COMMON_ARGS[@]}"
  "${BACKEND_SCRIPT_DIR}/publish.sh" "${COMMON_ARGS[@]}" --publish-mode "${BACKEND_PUBLISH_MODE}" "${PUBLISH_ARGS[@]}"
fi
