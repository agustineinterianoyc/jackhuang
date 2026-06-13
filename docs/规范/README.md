# 规范目录

`docs/规范/` 用于收口项目级规则真源。

这里放的是“以后都按什么规则执行”，不是某一次需求、某个模块或某次变更的事实记录。

## 当前结构

```text
docs/规范/
├── README.md
├── 需求管理规范.md
├── 变更记录规范.md
└── 数据库留痕规范.md
```

## 与其他文档的关系

- `docs/规范/AI开发SOP.md`
  作为统一执行流程真源，和其他规则文件一起收口到 `docs/规范/`。
- `docs/需求/`
  记录需求输入、模块总表、需求包计划和模块长期演进，是业务事实源。
- `docs/变更记录/`
  记录项目级变更事实，不再承担规则真源角色。
- `code/frontend/AGENTS.md`、`code/backend/AGENTS.md`
  只负责各自工程事实和入口说明。

## 使用原则

1. 规则优先写在这里，再由 `AGENTS.md`、工程级 `AGENTS.md` 或 skill 引用。
2. 如果只是补某次需求记录、某次变更说明或某个模块长期文档，不写到本目录。
3. 如果新增规则会影响 `project-init`、`backend-development-standard`、`requirement-implementation-tracking` 等项目级 skill，要同步检查对应 skill 和脚本中的路径事实。
