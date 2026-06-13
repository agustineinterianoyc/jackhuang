# 后端发布脚本模板

这套模板适用于常规后端项目，支持三类交付物：

- `archive`：目录或压缩包型产物
- `jar`：Java JAR/WAR 型产物
- `docker`：Docker 镜像型产物

包含文件：

- `common.sh`
- `check-env.sh`
- `build-base.sh`
- `publish.sh`
- `report.sh`

## 关键变量

- `BACKEND_PROJECT_DIR`
- `BACKEND_ARTIFACT_TYPE=archive|jar|docker`
- `BACKEND_BOOTSTRAP_CMD`
- `BACKEND_VALIDATE_CMD`
- `BACKEND_BUILD_CMD`
- `BACKEND_OUTPUT_PATH`
- `BACKEND_DOCKERFILE`
- `BACKEND_IMAGE_NAME`
- `BACKEND_IMAGE_TAG`
- `RELEASE_ACTIONS=publish,register,deploy`
- `REGISTER_CMD`
- `DEPLOY_CMD`

## 发布模式

- `archive` / `jar`：`local|http`
- `docker`：`registry`

## 使用方式

1. 根据项目交付物设置 `BACKEND_ARTIFACT_TYPE`
2. 替换对应构建命令和产物路径
3. 如为 Docker 镜像，补齐镜像名、tag、Dockerfile
4. 按需开启 `register` 和 `deploy` 阶段
