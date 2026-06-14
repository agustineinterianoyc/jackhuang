#!/bin/bash
# aldemohk 前端启动脚本
# 端口：默认 Vite 端口（5173）

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_CODE="aldemohk"

echo "=================================="
echo "  ${PROJECT_CODE} 前端启动"
echo "=================================="

cd "${SCRIPT_DIR}/../code/frontend"
npm run dev
