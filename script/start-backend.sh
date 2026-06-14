#!/bin/bash
# aldemohk 后端启动脚本
# 模块：aldemohk（业务应用模块）
# 端口：8080

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_CODE="aldemohk"
BACKEND_MODULE="aldemohk"

echo "=================================="
echo "  ${PROJECT_CODE} 后端启动"
echo "  模块: ${BACKEND_MODULE}"
echo "  端口: 8080"
echo "=================================="

cd "${SCRIPT_DIR}/../code/backend"
./mvnw -pl ${BACKEND_MODULE} -am spring-boot:run
