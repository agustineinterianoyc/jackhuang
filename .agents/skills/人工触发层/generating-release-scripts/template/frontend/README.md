# 前端发布脚本模板

这套模板适用于前端静态产物发布，默认围绕单个构建输出目录组织。

包含文件：

- `common.sh`
- `check-env.sh`
- `build-base.sh`
- `publish.sh`
- `report.sh`

## 适用场景

- Vite / Webpack / Next 静态导出 / Vue / React / Angular 等前端项目
- 构建产物最终落在单一目录，例如 `dist/`、`build/`、`out/`
- 发布动作以文件制品为主：本地目录、HTTP 文件服务、登记、部署触发

## 关键变量

- `FRONTEND_PROJECT_DIR`：前端项目目录，默认脚本上级目录
- `FRONTEND_BUILD_CMD`：构建命令，默认 `npm run build`
- `FRONTEND_VALIDATE_CMD`：最小校验命令，默认 `npm run type:check`
- `FRONTEND_BOOTSTRAP_CMD`：依赖安装命令，默认 `npm ci`
- `FRONTEND_OUTPUT_DIR`：构建输出目录，默认 `dist`
- `RELEASE_ACTIONS`：逗号分隔，支持 `publish,register,deploy`
- `REGISTER_CMD`：发布后登记命令
- `DEPLOY_CMD`：发布后部署触发命令

## 默认发布模式

- `local`：复制到目标目录
- `http`：上传归档、校验和、manifest 文件

## 使用方式

1. 复制 `frontend/` 到目标项目脚本目录
2. 替换构建命令、校验命令、输出目录
3. 根据需要选择 `RELEASE_ACTIONS`
4. 如需 HTTP 发布，补齐 `PUBLISH_BASE_URL`、`PUBLISH_UPLOAD_URL`、`PUBLISH_AUTH_TOKEN`
