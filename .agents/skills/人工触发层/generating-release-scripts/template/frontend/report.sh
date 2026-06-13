#!/usr/bin/env bash

set -euo pipefail

SCRIPT_NAME=""
STAGE_NAME=""
STATUS=""
VERSION=""
TARGET_ENV=""
MESSAGE=""

json_escape() {
  local value="${1:-}"
  value="${value//\\/\\\\}"
  value="${value//\"/\\\"}"
  value="${value//$'\n'/\\n}"
  value="${value//$'\r'/\\r}"
  value="${value//$'\t'/\\t}"
  printf '%s' "${value}"
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --script-name) SCRIPT_NAME="${2:-}"; shift 2 ;;
    --stage) STAGE_NAME="${2:-}"; shift 2 ;;
    --status) STATUS="${2:-}"; shift 2 ;;
    --version) VERSION="${2:-}"; shift 2 ;;
    --env) TARGET_ENV="${2:-}"; shift 2 ;;
    --message) MESSAGE="${2:-}"; shift 2 ;;
    --help) exit 0 ;;
    --dry-run) shift ;;
    *) exit 2 ;;
  esac
done

TIMESTAMP="$(date -u "+%Y-%m-%dT%H:%M:%SZ")"
printf '[report] %s stage=%s script=%s status=%s version=%s env=%s\n' "${TIMESTAMP}" "${STAGE_NAME}" "${SCRIPT_NAME}" "${STATUS}" "${VERSION}" "${TARGET_ENV}"
printf '{"component":"frontend","script_name":"%s","stage_name":"%s","status":"%s","version":"%s","environment":"%s","time":"%s","message":"%s"}\n' "$(json_escape "${SCRIPT_NAME}")" "$(json_escape "${STAGE_NAME}")" "$(json_escape "${STATUS}")" "$(json_escape "${VERSION}")" "$(json_escape "${TARGET_ENV}")" "$(json_escape "${TIMESTAMP}")" "$(json_escape "${MESSAGE}")"
