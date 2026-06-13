---
name: skill-routing-guide
description: 当用户想确认当前场景该用哪个项目级 skill、平时怎么问 AI 更容易命中正确流程，或需要整理项目级 skill 使用清单时使用。
---

# Skill 路由清单

## 概述

这个 skill 用来回答两个问题：

- 当前这个任务该用哪个项目级 skill
- 平时和 AI 对话时，怎样提问更容易稳定命中正确 skill

它本身不替代具体业务 skill。
当已经能明确命中某个专项 skill 时，应继续执行专项 skill，只把这里当作场景路由和提示词参考。

## 何时使用

- 用户想知道“这个场景该用哪个 skill”
- 用户想整理项目级 skill 使用清单
- 用户想把“怎么问 AI”沉淀成固定话术
- 用户不确定当前任务是需求拆分、后端开发、数据库变更、原型、发布还是多角色协作

## 使用方式

1. 先识别用户当前任务属于哪类场景
2. 先看下方精简路由表，再对照 `references/skill-usage-checklist.md` 获取详细话术
3. 同时给出一句适合直接复制的提示话术
4. 如果用户已经要求继续执行，就按匹配到的专项 skill 继续，不停留在抽象说明层

## 精简路由表

| 场景 | 推荐 skill | 必须使用的典型触发 |
| --- | --- | --- |
| 修改 `code/frontend/` 下正式前端代码 | `frontend-development-standard` | 页面、组件、路由、Pinia、类型、请求封装、样式、前端测试 |
| 修改 `code/backend/` 下 Java 后端代码 | `backend-development-standard` | Controller、Service、Repository、DTO/VO/DO、配置、测试、后端模块归属 |
| 数据库结构、DDL、Mapper SQL、初始化 SQL、DataSQL 留痕 | `database-schema-standard` | 表、字段、索引、SQL 方言、数据回填、数据库业务留痕 |
| 正式需求拆分、模块总表、需求包计划、实现进度 | `requirement-implementation-tracking` | 新需求、阶段性变更、模块拆分、实现计划、接口清单 |
| 多角色规划复核与实现修复交接 | `multi-model-delivery-loop` | 明确采用“规划复核角色 / 实现修复角色”协作 |
| 项目初始化 | `project-init` | 示例工程收敛为真实项目、包名/模块名/应用标识替换、残留清理 |
| JDK、Maven、Spring Boot / Cloud / Alibaba 版本基线 | `jdk-maven-matrix` | 初始化或父 POM 版本矩阵决策 |
| 产品评审用原型页面 | `prototype-page-builder` | 产出到 `docs/原型站点/` 的原型站点 |
| 前后端打包、构建、发布脚本 | `generating-release-scripts` | `check-env`、`build-base`、`publish`、`report` 脚本生成 |
| 需求、架构、规范、跨模块 Bug 等重要变更留痕 | `change-tracking` | 需要记录为什么改、影响什么、后续从哪里查 |
| 不确定该走哪个 skill | `skill-routing-guide` | 先问“这个任务该用哪个项目级 skill” |

## 组合使用顺序

| 场景 | 推荐顺序 |
| --- | --- |
| 完整正式需求开发 | `requirement-implementation-tracking` -> 具体前端/后端/数据库 skill -> `change-tracking` |
| 前端需求或页面改造 | `frontend-development-standard` -> `change-tracking` |
| 后端需求且涉及数据库 | `requirement-implementation-tracking` -> `backend-development-standard` + `database-schema-standard` -> `change-tracking` |
| 页面现象排查后落成前后端联动修复 | `frontend-development-standard` + `backend-development-standard` -> `change-tracking` |
| 多角色协作需求 | `requirement-implementation-tracking` -> `multi-model-delivery-loop` -> 具体实现 skill -> `change-tracking` |
| 项目初始化并选择后端技术基线 | `project-init` -> `jdk-maven-matrix` -> `change-tracking` |
| 产品原型任务 | `prototype-page-builder`；通常不进入正式前端工程流程 |

## 冲突处理

- 一个任务命中多个 skill 时，先执行“流程/规划类”skill，再执行“实现类”skill，最后执行“留痕类”skill。
- 需求拆分与实现计划的事实源是 `docs/需求/`；变更记录只记录项目级决策原因，不替代需求包计划。
- 数据库结构和 SQL 的事实源是 `code/backend/DataSQL/`；需求模块目录只做索引，不重复维护完整 SQL。
- 已经明确命中特定专项 skill 时，不要只停留在本路由清单，应继续进入专项执行。

## 输出要求

回答此类问题时，优先给出：

- 场景
- 推荐 skill
- 什么时候必须用
- 怎么对 AI 说
- 如需组合使用，先后顺序是什么

不要只罗列 skill 名称，不解释触发时机和提问方式。
