# 项目级 Skill 目录

这是本项目特定的 OpenCode Skill 仓库。

## 使用方式

项目级 skill 与全局 skill 使用方式相同。当执行与项目相关的任务时，系统会自动检查此处定义的 skill。

如果想先判断“当前任务该走哪个 skill、平时该怎么问 AI 更容易命中正确流程”，先看：

- `.agents/skills/条件触发层/skill-routing-guide/SKILL.md`
- `.agents/skills/条件触发层/skill-routing-guide/references/skill-usage-checklist.md`

## 逻辑分层

当前 skill 采用“逻辑分层 + 物理分层一致”的方式管理。

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

说明：

- 当前目录已经按逻辑层完成物理分层。
- 新增 skill 时，先确认它应该落在哪个逻辑层，再补路由说明和变更记录。

## 目录结构

```
.agents/skills/
├── 默认执行层/
├── 条件触发层/
├── 人工触发层/
│   └── SKILL_NAME/
│       ├── SKILL.md
│       └── supporting.*
└── README.md             # 本文件
```

## 创建新 Skill

1. 在对应逻辑层目录下创建新的 skill 目录
2. 添加 `SKILL.md` 文件，包含：
   - YAML Frontmatter：`name`（唯一标识）和 `description`（触发条件）
   - 概述：核心原则 1-2 句
   - 使用场景：何时使用、何时不用
   - 核心规则、标准流程、模板入口或支撑文件说明
   - 常见错误或收尾检查
   - 如该 skill 依赖其他 skill，应写清先后关系和事实源边界

3. 测试 skill 后提交

## 命名规范

- 使用短横线连接的小写英文目录名（kebab-case）：`feature-branch-cleanup`
- 目录名应和 `SKILL.md` frontmatter 中的 `name` 保持一致
- 优先使用能稳定表达场景的名词短语或动名词短语，例如 `backend-development-standard`、`change-tracking`
- 简洁明了：通常不超过 3-4 个词
- 不为了追求“动词开头”而重命名已经稳定使用的项目级 skill；如果确实需要改名，必须同步更新根 `AGENTS.md`、`docs/规范/AI开发SOP.md`、`skill-routing-guide` 和变更记录

## 参考

- Anthropic 最佳实践：见全局 skills `writing-skills/anthropic-best-practices.md`
