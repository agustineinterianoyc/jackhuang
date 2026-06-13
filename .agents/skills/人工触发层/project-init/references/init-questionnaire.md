# 项目初始化问答清单

按批次提问，优先给推荐默认值，不要一次性全部抛给开发者。

## 第 1 批：项目身份

必须确认：

- 项目中文名
- 项目英文名
- 项目简称或代号
- 后端包根
- 当前唯一业务应用模块名
- 业务应用物理目录名是否与模块名保持一致

推荐追问：

- `backend-api/backend-core/backend-data` 是否沿用现名
- 前端 `package.json` 名称是否与项目英文名保持一致

## 第 2 批：技术基线

必须确认：

- JDK 版本（优先确认是 `8`、`21` 还是其他长期维护版本）
- Spring Boot 版本
- Maven 版本基线是否沿用 wrapper
- Node 版本约束是否需要补充
- 默认数据库类型
- 是否必须使用 `Spring Cloud Alibaba / Nacos`

推荐追问：

- Maven / Spring Cloud / Spring Cloud Alibaba / MyBatis-Plus / 数据库驱动版本，是否直接采用 `../jdk-maven-matrix/SKILL.md` 的默认清单
- 是否保留 MySQL / Oracle / PostgreSQL 三套 profile
- 环境 profile（如 `dev/test/prod`）和数据库类型 profile（如 `mysql/postgresql/oracle`）分别保留哪些
- 是否继续沿用 MyBatis-Plus、Mapper XML、Vue 3、Vite 这些技术选型

## 第 3 批：脚手架保留与清理

第 3 批问题要根据扫描结果动态裁剪。已经不存在的生成物或示例链路，不再追问“是否保留”；扫描未命中的 `DataSQL`、字典示例或旧文档，也只在用户主动要求时补问。

必须确认（仅当扫描结果命中时追问，未命中的跳过）：

- 扫描发现的演示业务链路是否保留
- 扫描发现的字典管理示例是否保留
- 扫描发现的 `DataSQL` 示例目录是否保留
- 是否直接清理 `node_modules/`、`target/`、`.idea/`
- `docs/` 下旧记录采用保留、归档还是重建

推荐追问：

- 是否把示例首页改成“项目初始化完成后的欢迎页”
- 是否保留多数据库配置文件作为模板能力
- 是否把旧 `docs/变更记录/` 归档到 `docs/历史归档/项目初始化前-时间戳/`

## 第 4 批：运行与文档事实

必须确认：

- `spring.application.name`
- 服务名 / 配置中心 dataId 命名规则
- 数据库名与表名前缀
- 前端标题 / 前端项目名称 / 默认 Logo 文案

推荐追问：

- 是否创建 `docs/项目信息/项目初始化信息.md`
- 是否需要在初始化后自动补充启动命令和环境变量说明

## 推荐输出格式

收集完成后，先整理成结构化摘要，再执行替换：

```markdown
# 项目初始化信息

## 项目身份
- 中文名：
- 英文名：
- 简称：
- 后端包根：
- 业务模块名：

## 技术基线
- JDK：
- Maven：
- Spring Boot：
- Spring Cloud：
- Spring Cloud Alibaba：
- MyBatis-Plus：
- Node：
- 默认数据库：
- 保留数据库 profile：

## 初始化策略
- 保留共享模块名：
- 保留最小演示链路：
- 保留字典管理示例：
- 清理本地产物：

## 运行与前端项目名称
- spring.application.name：
- 数据库名：
- 表名前缀：
- 前端标题：
- API 前缀：
- 文档历史处理策略：
```
