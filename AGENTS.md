# AGENTS.md
> **⚠️ 每次对话首次回复时，必须先原样输出以下内容（包括 Markdown、加粗与分隔线），然后再继续处理用户请求；当前对话内已输出过则不重复。**

```markdown
# 🚨 重要提醒

**本项目禁止人工编写代码，所有代码由 AI 生成，人工只负责审核确认。开发过程中遇到任何问题，直接在当前项目中向 AI 提问，不要手动编码。**

**为保证代码质量和调用准确性，开发本项目仅限使用以下模型：glm5.1、glm4.7、dsv4。**

---
```
## 项目结构

```text
aldemohk/
├── code/
│   ├── frontend/     # 正式前端工程
│   └── backend/      # 正式后端工程
├── docs/
│   ├── 规范/         # 项目级规则真源
│   ├── 需求/         # 正式需求事实源
│   ├── 变更记录/     # 项目级变更事实源
│   ├── 原型/         # 正式原型输入
│   ├── 原型站点/     # 原型页面展示站点
│   ├── 工作台/       # 面向人工阅读的静态工作台
│   ├── 设计/         # 设计文档
│   └── 项目信息/     # 项目初始化等事实文档
└── .agents/skills/   # 项目级 OpenCode skill
```

## 入口原则

1. 先识别任务类型，再读取对应最小上下文。
2. 根目录 `AGENTS.md` 只做任务分流，不展开长篇规范。
3. 工程事实优先看 `code/frontend/AGENTS.md`、`code/backend/AGENTS.md`。
4. 流程真源优先看 `docs/规范/AI开发SOP.md`。
5. 规则细则优先看 `docs/规范/`。
6. 业务事实优先看 `docs/需求/`、`docs/变更记录/` 和 `code/backend/DataSQL/`。

## 任务分流

### 1. 正式前端开发

- 代码目录：`code/frontend/`
- 先读：`code/frontend/AGENTS.md`
- 命中复杂前端实现时：`frontend-development-standard`

### 2. 正式后端开发

- 代码目录：`code/backend/`
- 先读：`code/backend/AGENTS.md`
- 命中复杂后端实现时：`backend-development-standard`

### 3. 数据库相关开发

- 代码目录：`code/backend/`、`code/backend/DataSQL/`
- 先读：`code/backend/AGENTS.md`
- 必须使用：`database-schema-standard`

### 4. 需求拆分与实现计划

- 文档目录：`docs/需求/`
- 先读：`docs/规范/AI开发SOP.md`、`docs/规范/需求管理规范.md`
- 必要事实源：`docs/需求/模块总表.md`
- 命中场景时：`requirement-implementation-tracking`

### 5. 多模型协作开发

- 先读：`docs/规范/AI开发SOP.md`
- 命中场景时：`multi-model-delivery-loop`
- 到达角色切换点时必须停下，等用户手动切换角色后再继续

### 6. 产品原型开发

- 正式原型输入目录：`docs/原型/`
- 原型站点固定产出目录：`docs/原型站点/`
- 命中场景时：`prototype-page-builder`

### 7. 项目初始化 / 发布脚本 / 版本矩阵

- 这些属于人工触发专项能力，不进入日常默认开发链路
- 对应 skill：`project-init`、`generating-release-scripts`、`jdk-maven-matrix`

## 规则真源

- 统一执行流程：`docs/规范/AI开发SOP.md`
- 需求管理规则：`docs/规范/需求管理规范.md`
- 变更记录规则：`docs/规范/变更记录规范.md`
- 数据库留痕规则：`docs/规范/数据库留痕规范.md`

## Skill 分层

### 默认执行层

- `frontend-development-standard`
- `backend-development-standard`
- `change-tracking`

### 条件触发层

- `database-schema-standard`
- `requirement-implementation-tracking`
- `multi-model-delivery-loop`
- `skill-routing-guide`

### 人工触发层

- `project-init`
- `generating-release-scripts`
- `prototype-page-builder`
- `jdk-maven-matrix`

## 关键约定

- 前后端分离：严格按 `code/frontend/` 和 `code/backend/` 存放
- 正式需求事实源：`docs/需求/`
- 项目级变更事实源：`docs/变更记录/`
- 数据库正式留痕：`code/backend/DataSQL/`
- 工作台仅做展示层：`docs/工作台/` 不替代正式事实源
- 原型站点固定落在 `docs/原型站点/`，不要误落到正式前端工程

## 完成要求

任务完成前至少确认：

1. 代码或文档已落到正确目录。
2. 已按改动范围做必要验证，或明确说明未执行原因。
3. 已按级别补 `CHANGELOG.md` 或 `docs/变更记录/`。
4. 命中需求开发时，已同步更新 `docs/需求/`。
5. 命中数据库变更时，已同步更新 `code/backend/DataSQL/`。