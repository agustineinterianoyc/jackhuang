#!/usr/bin/env bash
# 公司培训项目（aldemo）后端启动脚本
# 模块：aldemo（业务应用模块）
# 默认 profile：dev,mysql
set -euo pipefail

PROJECT_NAME="公司培训项目"
PROJECT_CODE="aldemo"
BACKEND_MODULE="aldemo"
SERVER_PORT_DEFAULT=8080

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
BACKEND_DIR="${PROJECT_ROOT}/code/backend"

echo "========================================"
echo " 项目     : ${PROJECT_NAME} (${PROJECT_CODE})"
echo " 模块     : ${BACKEND_MODULE}"
echo " 默认端口 : ${SERVER_PORT_DEFAULT}"
echo " 工作目录 : ${BACKEND_DIR}"
echo "========================================"

cd "${BACKEND_DIR}"

if [[ ! -x "./mvnw" ]]; then
  chmod +x "./mvnw" || true
fi

exec ./mvnw -pl "${BACKEND_MODULE}" -am spring-boot:run "$@"
