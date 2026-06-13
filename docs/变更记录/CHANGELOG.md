# CHANGELOG

> 项目级时间倒序变更主线。每条变更必须包含日期、类别、摘要、影响范围。

## 模板示例

```markdown
## 2026-06-13（下午）

- 变更类别：架构变更
- 变更摘要：
- 影响范围：
- 关联文档：
- 决策原因：
```

## 2026-06-13（下午）

- 变更类别：项目初始化
- 变更摘要：基于 `ai-cli-template` 脚手架初始化为公司培训项目（aldemo）。完成包根、模块名、Nacos / 数据库 / 前端项目名等项目事实统一替换；归档旧 docs 内容；重置 DataSQL 汇总清单为初始骨架；为父 POM 和业务模块分别配置 `spring-boot-maven-plugin` 跳过/启用策略。
- 影响范围：`code/backend/`（整个后端工程）、`code/frontend/`（package.json、index.html、Layout、router、test 脚本）、`docs/`（重建变更记录/需求/原型/原型站点/设计/工作台骨架，旧内容归档至 `docs/历史归档/项目初始化前-{时间戳}/`）、`.agents/skills/` 与 `.agents/description/` 中的项目事实占位
- 关联文档：`docs/项目信息/项目初始化信息.md`、`docs/项目信息/初始化任务清单.md`
- 决策原因：作为正式开发起点，统一项目身份与目录、配置、文档事实
