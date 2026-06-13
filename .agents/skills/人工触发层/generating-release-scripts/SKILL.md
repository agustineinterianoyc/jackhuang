---
name: generating-release-scripts
description: 当用户要求为前后端项目生成打包、构建或发布脚本时使用 — 适用于前端静态产物、后端 archive 或 jar/war、Docker 镜像，以及分层的 publish/register/deploy 流程
---

# 生成前后端发布脚本

## 概述

生成适用于前后端分离项目的 shell 发布脚本。前端和后端各自保留 `check-env`、`build-base`、`publish`、`report` 的清晰职责边界，再由独立的总控脚本串联执行。

## 何时使用

- 用户要求为前端和后端分别生成发布脚本
- 用户需要同时覆盖前端静态产物、后端目录包、JAR/WAR、Docker 镜像
- 用户描述的流程不仅要发布制品，还要登记发布记录或触发部署

## 适用边界

- 适合：前端静态站点、常规后端服务、JAR/WAR、Docker 镜像
- 不适合：复杂多服务矩阵、专有平台深度 API、Windows 原生脚本
- 当前模板全部是 Bash 脚本，Windows 环境需要 Git Bash / WSL / CI Bash Runner 执行；如果用户明确要求 PowerShell 原生发布脚本，应另建 PowerShell 模板，不要把 `.sh` 模板直接改成混合语法

## 核心模式

### 强制 4 脚本拆分

| 脚本 | 只做 | 禁止做 |
|------|------|--------|
| `check-env.sh` | 验证环境、参数、凭证、目录、端点可达性 | 构建或发布 |
| `build-base.sh` | 安装依赖、类型检查、构建、归档产物、生成校验和 + 清单 | 上传或部署 |
| `publish.sh` | 读取已有产物、检查目标是否存在（一致则跳过，不同则失败）、发布 | 重新构建 |
| `report.sh` | 输出阶段开始/结束日志、错误摘要、结果摘要、结构化 JSON | 业务逻辑 |

### 串联顺序

```
frontend/check-env  →  frontend/build-base  →  frontend/publish
backend/check-env   →  backend/build-base   →  backend/publish
                          ↑
                  (每步各自调用 report.sh)

orchestrator/orchestrate.sh 只负责串联两套流程
```

## 快速参考

### 共享函数（放在 common.sh）

| 函数 | 用途 |
|------|------|
| `log_info/log_warn/log_error` | 带时间戳的日志，错误输出到 stderr |
| `die <code> <args...> <msg>` | 记录错误 + 发送报告 + 非零退出 |
| `emit_report <script> <stage> <status> <ver> <env> [msg]` | 调用 report.sh |
| `run_cmd <cmd...>` | 执行命令或 dry-run 打印 |
| `require_command <name>` | 检查命令是否存在 |
| `require_env_var <name>` | 检查环境变量是否存在（绝不打印值） |
| `validate_target_env <env>` | 仅允许 dev/test/staging/prod |
| `validate_version <ver>` | 字母数字 + 点/连字符/下划线 |

### 退出码

| 码 | 含义 |
|----|------|
| 0 | 成功 |
| 2 | 参数错误 |
| 3 | 环境检查失败 |
| 4 | 构建失败 |
| 5 | 发布失败 |
| 6 | 运行时错误 |

### 通用参数

```
--env <dev|test|staging|prod>       必填
--version <版本号>                    必填（字母数字 + ._-）
--artifact-name <名称>               按组件定义默认值
--publish-mode <local|http|registry> 取决于产物类型
--install-deps <auto|always|never>   默认: auto
--skip-tests                         跳过类型检查
--confirm-prod                       生产发布必须
--dry-run                            仅打印不执行
--help                               显示帮助
```

### 环境变量

| 变量 | 用途 |
|------|------|
| `PUBLISH_TARGET_DIR` | 本地发布目标目录（默认 `published/<env>`） |
| `PUBLISH_BASE_URL` | HTTP 只读基地址 |
| `PUBLISH_UPLOAD_URL` | HTTP 上传地址 |
| `PUBLISH_AUTH_TOKEN` | 认证令牌 — 仅检查是否存在，绝不打印值 |
| `RELEASE_APPROVER` | 生产环境必须设置 |

## 模板选择

- 前端静态产物：使用 `template/frontend/`
- 后端目录包或压缩包：使用 `template/backend/`，并设置 `BACKEND_ARTIFACT_TYPE=archive`
- 后端 JAR/WAR：使用 `template/backend/`，并设置 `BACKEND_ARTIFACT_TYPE=jar`
- 后端 Docker 镜像：使用 `template/backend/`，并设置 `BACKEND_ARTIFACT_TYPE=docker`
- 前后端一起发布：再加 `template/orchestrator/`

## 脚本模板

所有脚本通过 source 引入 common.sh：

```bash
source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"
```

每份脚本遵循以下结构：
1. 设置默认值
2. `while` 循环解析参数
3. 校验必填参数（不合法则 die + EXIT_INVALID_ARGS）
4. `create_runtime_tmpdir` + `trap cleanup_runtime EXIT`
5. `emit_report ... started`
6. 主体逻辑带错误检查
7. `emit_report ... succeeded`

## 常见错误

1. **职责混杂** — check-env 去构建、build-base 去发布、report 里放业务逻辑
2. **缺少 dry-run** — 每份脚本必须支持 `--dry-run`
3. **缺少生产保护** — prod 环境必须同时检查 `RELEASE_APPROVER` 环境变量和 `--confirm-prod` 参数
4. **覆盖已发布制品** — 校验和一致 → 跳过（幂等）；校验和不同 → 失败退出
5. **泄露凭证** — 绝不可打印 `PUBLISH_AUTH_TOKEN` 的值
6. **缺少清理** — 必须用 `trap cleanup_runtime EXIT` 清理临时目录
7. **缺少报告** — 每份脚本必须在开始、成功、失败时调用 emit_report
8. **硬编码路径** — 必须使用脚本目录或组件目录相对路径
9. **把 publish/register/deploy 混为一谈** — 三层动作必须显式分开
10. **后端产物类型不明确** — archive、jar、docker 必须先定类型再生成脚本

## 支撑文件

skill 目录下保留三套可复用模板：

- `约定规范.md` — 生成脚本时必须遵守的完整规范

### template/frontend/

- `template/frontend/README.md`
- `template/frontend/common.sh`
- `template/frontend/check-env.sh`
- `template/frontend/build-base.sh`
- `template/frontend/publish.sh`
- `template/frontend/report.sh`

### template/backend/

- `template/backend/README.md`
- `template/backend/common.sh`
- `template/backend/check-env.sh`
- `template/backend/build-base.sh`
- `template/backend/publish.sh`
- `template/backend/report.sh`

### template/orchestrator/

- `template/orchestrator/README.md`
- `template/orchestrator/orchestrate.sh`

## 使用建议

1. 先读 `约定规范.md`，明确职责边界。
2. 按项目形态选择 `frontend`、`backend`、`orchestrator` 模板。
3. 真正产出时优先从对应模板复制，再按组件命令、产物目录、发布方式替换占位值。
