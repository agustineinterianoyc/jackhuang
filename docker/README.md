# Docker 启动指南（公司培训项目 / aldemo）

## 适用范围

- 后端（Spring Boot）+ 前端（Vite dev server）容器化启动
- 数据库复用宿主机 MySQL（通过 `host.docker.internal:3306` 访问）

## 前置条件

- 安装 Docker Desktop（Windows / macOS）或 Docker Engine + Compose 插件（Linux）
- 宿主机 MySQL 在 `127.0.0.1:3306` 已就绪，账号默认 `root` / `123456`
- 端口 `8080`、`5173` 在宿主机未被占用

## 文件清单

| 文件 | 说明 |
|---|---|
| `docker/Dockerfile.backend` | 后端开发镜像：JDK 21 + Maven，容器内执行 `mvn -pl aldemo -am spring-boot:run` |
| `docker/Dockerfile.frontend` | 前端开发镜像：Node 22-alpine，容器内执行 `npm run dev` |
| `docker/docker-compose.yml` | 编排：backend + frontend，源码挂载、依赖卷化 |
| `docker/.env.example` | 环境变量样例（拷贝为 `.env` 使用） |
| `docker/.dockerignore` | 构建上下文忽略规则 |
| `script/start-docker.cmd|sh` | 一键启动 |
| `script/stop-docker.cmd|sh` | 一键停止 |

## 快速开始

### 1. 拷贝并按需修改环境变量

```powershell
copy docker\.env.example docker\.env
```

### 2. 启动

```powershell
script\start-docker.cmd
```

或直接：

```powershell
docker compose -f docker/docker-compose.yml --env-file docker/.env up -d --build
```

启动后：

- 后端：http://localhost:8080
- 前端：http://localhost:5173
- 健康检查：http://localhost:8080/actuator/health

### 3. 查看日志

```powershell
docker compose -f docker/docker-compose.yml logs -f backend
docker compose -f docker/docker-compose.yml logs -f frontend
```

### 4. 停止

```powershell
script\stop-docker.cmd
```

或：

```powershell
docker compose -f docker/docker-compose.yml down
```

## 常见问题

### MySQL 连接失败

- 容器内通过 `host.docker.internal:3306` 访问宿主机；Linux 宿主已通过 `extra_hosts: host.docker.internal:host-gateway` 注入
- 若宿主 MySQL 仅监听 `127.0.0.1`，需放开监听到 `0.0.0.0` 或允许容器网段
- 账号 `root` 需允许从容器 IP 登录；如限制为 `localhost`，请创建 `'root'@'%'` 或专用账号

### 前端 HMR 不工作

- Vite 默认监听 `0.0.0.0`，已在 Dockerfile 中加 `--host 0.0.0.0`
- Windows 下文件系统事件可能无法穿透 WSL2，可在 `vite.config.ts` 中开启 `server.watch.usePolling: true`

### 想启用 Nacos

- 在 `docker/.env` 中设置：

  ```
  NACOS_DISCOVERY_ENABLED=true
  NACOS_CONFIG_ENABLED=true
  SERVICE_REGISTRY_AUTO_REGISTRATION=true
  SERVICE_DISCOVERY_ENABLED=true
  ```

- 并在 compose 文件 backend.environment 中追加 `NACOS_SERVER_ADDR=host.docker.internal:8848` 等

## 数据持久化

- Maven 本地仓库：命名卷 `aldemo-maven-repo`
- 前端 node_modules：命名卷 `aldemo-frontend-node-modules`
- 源码：bind mount 自宿主仓库目录，编辑即生效
