#!/bin/bash
# aldemohk 前后端联合启动脚本
# Ctrl+C 同时停止两个进程

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_CODE="aldemohk"

echo "=================================="
echo "  ${PROJECT_CODE} 前后端联合启动"
echo "  Ctrl+C 停止所有服务"
echo "=================================="

cleanup() {
    echo ""
    echo "正在停止所有服务..."
    kill 0
}

trap cleanup INT TERM

"${SCRIPT_DIR}/start-backend.sh" &
"${SCRIPT_DIR}/start-frontend.sh" &

wait
