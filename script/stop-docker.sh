#!/usr/bin/env bash
# aldemo docker stopper (Linux/macOS/Git Bash)
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
COMPOSE_FILE="${ROOT_DIR}/docker/docker-compose.yml"

echo "========================================"
echo " 正在停止 aldemo 容器 ..."
echo "========================================"

cd "${ROOT_DIR}"
docker compose -f "${COMPOSE_FILE}" down
