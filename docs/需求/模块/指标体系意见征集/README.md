---
module: opinion
status: 待开始
latest_package: 2026-06-opinion-baseline
prototype_batch: 20260613-opinion-v2.1
---

# 模块 README — 指标体系意见征集

## 1. 模块基础信息

- 模块中文名：指标体系意见征集
- 模块编码：`opinion`
- 后端目录名：`code/backend/aldemo/src/main/java/com/hk/demo/app/{controller,service,repository,mapper,model/*}/opinion/`
- DataSQL 目录名：`code/backend/DataSQL/opinion/`
- 当前状态：待开始

## 2. 模块职责

- **业务对象**：征集任务（survey）/ 征集模块（module，5 个 Tab）/ 征集对象（target）/ 基层任务（unit_task）/ 基层意见（item）/ 专业任务（dept_task）/ 专业反馈（feedback）/ 汇总采纳（summary_item）/ 附件（attachment）/ 提醒（reminder_log）/ 审计（action_log）
- **核心流程**：10 节点工作流（草稿 → 待填报 → 填报中 → 专业反馈中 → 已完成 → 已发布）+ 6 主状态机 + 4 套子状态
- **主要页面**：P01 意见征集管理 / P02 基层单位意见征集 / P03 基层单位意见征集审核 / P04 专业部门反馈 / P05 专业部门反馈审核
- **主要接口**：38 个（API-101 ～ API-902），统一前缀 `/api/opinion/`

## 3. 子模块边界

| 子模块 | 子模块编码 | 主页面 | 主角色 | 关键能力 |
| --- | --- | --- | --- | --- |
| 意见征集管理 | `opinion.survey` | P01 | R01 | 征集任务 CRUD、开启、推进、汇总、发布 |
| 基层意见填报 | `opinion.unit-fill` | P02 | R02 | 基层填报 / 提交 / 查看 |
| 基层意见审核 | `opinion.unit-audit` | P03 | R03 | 基层审核 / 退回 |
| 专业部门反馈 | `opinion.dept-feedback` | P04 | R04 | 专业反馈 / 采纳决策 / 提交 |
| 专业部门反馈审核 | `opinion.dept-audit` | P05 | R05 | 专业审核 / 退回 |
| 进度查询与提醒 | `opinion.progress` | P01 弹窗 | R01 | 进度弹窗 + 一键 / 单条提醒 |
| 汇总发布 | `opinion.summary` | P01 汇总页 | R01 | 汇总采纳 / 调整后内容 / 发布 / 模板导入导出 |
| 附件管理 | `opinion.attachment` | 跨页面 | 全角色 | 模块附件 / 汇总附件 |

## 4. 关联需求包

| 需求包 | 本模块承担内容 | 状态 | 计划文档 |
| --- | --- | --- | --- |
| `2026-06-opinion-baseline` | 8 个子模块全量首期实现（9 个 STEP） | 待开始 | `docs/需求/需求包/2026-06-opinion-baseline/README.md` |

## 5. 关联设计文档

- 设计说明书：`docs/设计/指标体系意见征集模块/设计说明书 指标体系意见征集模块.md`
- 数据库设计说明书：`docs/设计/指标体系意见征集模块/数据库设计说明书 指标体系意见征集模块.md`
- 接口清单（设计层）：`docs/设计/指标体系意见征集模块/接口清单 指标体系意见征集模块.md`
- 需求实现计划（设计层）：`docs/设计/指标体系意见征集模块/需求实现计划 指标体系意见征集模块.md`

## 6. 关联文档

- 需求实现记录：`需求实现记录.md`
- 需求实现优化调整记录：`需求实现优化调整记录.md`
- 数据库留痕索引：`数据库留痕索引.md`
