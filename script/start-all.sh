#!/usr/bin/env bash
# 公司培训项目（aldemo）前后端联合启动脚本
# 行为：在后台同时启动后端与前端，并在 Ctrl+C 时一起停止。
set -euo pipefail

PROJECT_NAME="公司培训项目"
PROJECT_CODE="aldemo"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "========================================"
echo " 项目: ${PROJECT_NAME} (${PROJECT_CODE})"
echo " 同时启动: 后端 + 前端"
echo " 停止方式: Ctrl+C"
echo "========================================"

PIDS=()

cleanup() {
  echo ""
  echo "[info] 收到停止信号，结束所有子进程 ..."
  for pid in "${PIDS[@]:-}"; do
    if [[ -n "${pid:-}" ]] && kill -0 "${pid}" 2>/dev/null; then
      kill "${pid}" 2>/dev/null || true
    fi
  done
  wait 2>/dev/null || true
  echo "[info] 已退出。"
}

trap cleanup INT TERM EXIT

bash "${SCRIPT_DIR}/start-backend.sh" &
PIDS+=("$!")

bash "${SCRIPT_DIR}/start-frontend.sh" &
PIDS+=("$!")

wait -n "${PIDS[@]}" || true
wait || true
