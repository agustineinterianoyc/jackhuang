#!/usr/bin/env bash
# 公司培训项目（aldemo）前端启动脚本
# 默认开发端口：5173（由 Vite 决定）
set -euo pipefail

PROJECT_NAME="公司培训项目"
PROJECT_CODE="aldemo"
FRONTEND_DEV_PORT_DEFAULT=5173

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
FRONTEND_DIR="${PROJECT_ROOT}/code/frontend"

echo "========================================"
echo " 项目     : ${PROJECT_NAME} (${PROJECT_CODE})"
echo " 默认端口 : ${FRONTEND_DEV_PORT_DEFAULT}"
echo " 工作目录 : ${FRONTEND_DIR}"
echo "========================================"

cd "${FRONTEND_DIR}"

if [[ ! -d "node_modules" ]]; then
  echo "[info] node_modules 未安装，先执行 npm install ..."
  npm install
fi

exec npm run dev -- "$@"
