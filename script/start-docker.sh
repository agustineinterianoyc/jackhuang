#!/usr/bin/env bash
# aldemohk docker launcher (Linux/macOS/Git Bash)
set -euo pipefail

PROJECT_NAME="公司培训项目"
PROJECT_CODE="aldemohk"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
COMPOSE_FILE="${ROOT_DIR}/docker/docker-compose.yml"
ENV_FILE="${ROOT_DIR}/docker/.env"

echo "========================================"
echo " 项目     : ${PROJECT_NAME} (${PROJECT_CODE})"
echo " Compose : ${COMPOSE_FILE}"
echo "========================================"

if [[ ! -f "${ENV_FILE}" ]]; then
  echo "[info] docker/.env 不存在，从 .env.example 复制 ..."
  cp "${ROOT_DIR}/docker/.env.example" "${ENV_FILE}"
fi

cd "${ROOT_DIR}"
docker compose -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" up -d --build

cat <<EOF

========================================
 后端     : http://localhost:8080
 前端     : http://localhost:5173
 健康检查 : http://localhost:8080/actuator/health
========================================
 日志: docker compose -f docker/docker-compose.yml logs -f backend
 停止: script/stop-docker.sh
========================================
EOF
