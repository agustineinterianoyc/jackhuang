# AI 开发 SOP

适用范围：当前仓库内由 AI 参与的前端、后端、数据库、原型与文档类开发任务。

目标：把“识别任务、读取最小上下文、按边界实现、完成验证与留痕”收敛成统一流程，避免每次都把整套脚手架规则一次性带进上下文。

## 1. 总原则

1. 先判断任务类型，再动手实现。
2. 先读最小必要上下文，不默认展开全部文档和 skill。
3. 代码修改不等于任务完成，验证和留痕是交付的一部分。
4. 不为了“看起来更高级”主动扩大重构范围。
5. 涉及需求、架构、规范、Bug 追溯或数据库结构变化时，必须同步更新记录文档。

## 2. 默认阅读链路

### 2.1 前端开发

1. `AGENTS.md`
2. `code/frontend/AGENTS.md`
3. 命中复杂前端场景时再读 `frontend-development-standard`

### 2.2 后端开发

1. `AGENTS.md`
2. `code/backend/AGENTS.md`
3. 命中复杂后端场景时再读 `backend-development-standard`

### 2.3 数据库开发

1. `AGENTS.md`
2. `code/backend/AGENTS.md`
3. `database-schema-standard`
4. 必要时看 `docs/规范/数据库留痕规范.md`

### 2.4 需求拆分与实现计划

1. `AGENTS.md`
2. `docs/规范/需求管理规范.md`
3. `docs/需求/模块总表.md`
4. `requirement-implementation-tracking`

### 2.5 多模型协作

1. `AGENTS.md`
2. `multi-model-delivery-loop`
3. 到达角色切换点时更新进度文档后暂停，等待用户手动切换角色

### 2.6 原型、初始化、发布、版本矩阵

这些都属于专项能力，仅在用户明确提出目标时进入，不作为默认开发上下文。

## 3. 任务分类

### 3.1 正式前端开发

- 代码目录：`code/frontend/`
- 工程事实入口：`code/frontend/AGENTS.md`
- 规则细则：`frontend-development-standard`

### 3.2 正式后端开发

- 代码目录：`code/backend/`
- 工程事实入口：`code/backend/AGENTS.md`
- 规则细则：`backend-development-standard`

### 3.3 数据库相关开发

- 代码目录：`code/backend/`、`code/backend/DataSQL/`
- 工程事实入口：`code/backend/AGENTS.md`
- 规则细则：`database-schema-standard`

### 3.4 产品原型开发

- 正式原型输入目录：`docs/原型/`
- 原型站点固定产出目录：`docs/原型站点/`
- 规则细则：`prototype-page-builder`

### 3.5 纯文档或规范调整

- 流程入口：本文件
- 规则真源目录：`docs/规范/`
- 项目级变更记录：`docs/变更记录/`

### 3.6 需求拆分与实现计划

- 文档目录：`docs/需求/`
- 规则细则：`docs/规范/需求管理规范.md`
- 执行 skill：`requirement-implementation-tracking`

### 3.7 多模型协作开发

- 文档目录：`docs/需求/需求包/`
- 执行 skill：`multi-model-delivery-loop`

## 4. 标准执行流程

### 第 1 步：识别任务类型

先判断属于前端、后端、数据库、需求、原型、规范还是人工触发专项。

如果任务跨前后端、跨模块或跨数据库，不按“小修改”处理。

### 第 2 步：读取最小上下文

至少读取：

1. 根 `AGENTS.md`
2. 对应工程 `AGENTS.md` 或对应规范文档
3. 命中时再读取对应 skill

不要默认把所有 skill、所有规范文档和所有需求文档一次性展开。

### 第 3 步：判断落点

- 前端：按 `views`、`components`、`stores`、`types`、`utils` 等既有结构落位
- 后端：先判断属于 `backend-api`、`backend-core`、`backend-data` 还是 `aldemohk`
- 需求类任务：先查 `docs/需求/模块总表.md`
- 数据库类任务：同步判断 `DataSQL` 业务目录落点

### 第 4 步：按最小改动实现

1. 先阅读相邻文件，保持与周边风格一致。
2. 复用既有返回结构、异常风格、配置方式和测试方式。
3. 不顺手做无关重构。

### 第 5 步：补验证

前端按需执行：

- `npm run lint`
- `npm run type-check`
- `npm run build`
- `npm run test:unit`

后端按需执行：

- `./mvnw test`
- `./mvnw clean verify`
- 对应模块测试
- 启动验证或接口回归验证

如果改动涉及配置或 SQL，除测试外还要补至少一种额外验证。

### 第 6 步：补留痕

1. 按 `docs/规范/变更记录规范.md` 判断写模块 `CHANGELOG.md` 还是 `docs/变更记录/`
2. 命中需求开发场景时，同步更新 `docs/需求/`
3. 命中数据库变更时，同步更新 `code/backend/DataSQL/`
4. 命中多模型协作时，同步更新需求包进度中的角色切换状态和问题说明

### 第 7 步：确认完成定义

只有同时满足以下条件，任务才算完成：

1. 代码或文档已按正确目录落位。
2. 改动与周边风格一致。
3. 必要验证已完成，或已明确说明未执行原因。
4. 变更记录已按级别补齐。
5. 命中需求开发场景时，`docs/需求/` 已同步更新。
6. 命中数据库开发场景时，`DataSQL` 已同步更新。

## 5. 规则真源索引

- 需求管理：`docs/规范/需求管理规范.md`
- 变更记录：`docs/规范/变更记录规范.md`
- 数据库留痕：`docs/规范/数据库留痕规范.md`

## 6. 常见错误

- 还没判断任务类型，就把所有规范一起读一遍
- 只改代码，不更新 `CHANGELOG` 或 `docs/变更记录/`
- 需要需求计划时没更新 `docs/需求/`
- 改了 SQL 或表结构，但没更新 `DataSQL`
- 到了多模型切换点却没有先更新进度文档
- 把原型任务误做进正式前端工程
