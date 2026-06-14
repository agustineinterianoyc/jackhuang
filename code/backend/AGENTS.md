# AGENTS.md

## 定位

- `code/backend/` 是当前项目的后端父工程目录。
- 它是 Maven 多模块脚手架根目录，不是单体源码目录。
- 当前默认包根是 `com.hk.demo`。
- `backend-api`、`backend-core`、`backend-data`、`aldemohk` 是固定模块边界。

## Skill 约束

- 修改 `code/backend/` 下的后端代码、配置、SQL、测试时，先读本文件，再按需使用：
  - `.agents/skills/默认执行层/backend-development-standard/SKILL.md`
  - `.agents/skills/条件触发层/database-schema-standard/SKILL.md`
- 本文件负责后端工程事实入口；分层边界、测试细则、数据库留痕细则由对应 skill 收口。

## 根目录结构

```text
code/backend/
├── pom.xml
├── .mvn/
├── mvnw
├── mvnw.cmd
├── AGENTS.md
├── backend-api/
├── backend-core/
├── backend-data/
├── aldemohk/
└── DataSQL/
```

## 模块职责

### `backend-api`

- 放跨模块共享的纯 Java 契约类型
- 默认不放运行期 Spring 组件

### `backend-core`

- 放后端运行期基础设施代码
- 放统一异常处理、trace、Web 层公共配置和公共工具

### `backend-data`

- 放数据访问层共享能力
- 放 DO 基类、分页抽象、共享 Mapper 或数据层配置

### `aldemohk`

- 当前唯一可运行业务应用模块
- 默认新增业务代码都先落在这里

## 新增代码的默认判断顺序

1. 公共契约 -> `backend-api`
2. 基础设施 -> `backend-core`
3. 通用数据能力 -> `backend-data`
4. 当前业务逻辑 -> `aldemohk`

如果不确定未来是否复用，默认先放 `aldemohk`。

## 需求模块与后端目录命名

- 正式业务需求开发前，先查 `docs/需求/模块总表.md`
- 模块中文名、模块编码、后端目录名和 DataSQL 目录名，以模块总表为唯一事实源
- 业务应用模块中的新增业务代码，优先按模块编码建立子目录

示例：`部门管理 = dept`

```text
controller/dept/
service/dept/
service/dept/impl/
repository/dept/
repository/dept/impl/
mapper/dept/
model/request/dept/
model/response/dept/
model/vo/dept/
model/dataobject/dept/
resources/mapper/dept/
src/test/java/.../app/dept/
src/test/resources/test-report/dept/
```

## DataSQL

- `code/backend/DataSQL/` 是数据库结构、字段、索引、SQL 和变更流水的正式留痕目录
- `业务变更汇总清单.md` 负责业务级总览
- 各业务目录负责具体表设计与变更记录
- 数据库留痕规则详见 `docs/规范/数据库留痕规范.md`

## 默认数据库事实

- 数据库类型判断优先读取：
  `aldemohk/src/main/resources/application.yaml`
- 当前脚手架默认：`spring.profiles.active=dev,mysql`
- 没有额外说明时，默认按 MySQL 方言处理 SQL

## 依赖方向

固定依赖方向：

```text
backend-api <- backend-core <- backend-data <- aldemohk
```

不要让共享模块反向依赖业务应用。

## 构建与运行

统一从 `code/backend/` 根目录执行：

```powershell
./mvnw test
./mvnw clean verify
./mvnw -pl aldemohk -am spring-boot:run
```

工程要求：

- JDK 21
- 默认运行组合：`dev + mysql`
- 默认测试组合：`test + mysql`

## AI 阅读后端时的默认理解

1. `pom.xml` 是整个后端的构建入口。
2. `backend-api`、`backend-core`、`backend-data` 是共享模块。
3. `aldemohk` 是当前唯一可运行应用。
4. `.gitkeep` 代表目录模板已固定，不是可随意删除的噪音文件。

## 禁止事项

- 不要把所有业务模型提前挪到共享模块。
- 不要让共享模块反向依赖业务应用。
- 不要把业务 Controller、Service 实现放进 `backend-core` 或 `backend-data`。
- 不要绕过父工程在子模块各自维护独立构建约定。
