# 指标体系意见征集 — 模块 README

## 基本信息

| 项目 | 内容 |
| --- | --- |
| 模块中文名 | 指标体系意见征集 |
| 模块编码 | `opinion` |
| 项目名称 | aldemohk |
| 当前需求包 | 2026-06-opinion-v2.1 |
| 设计基线 | `docs/设计/指标体系意见征集模块/`（4 份设计文档 v1.0） |
| 当前状态 | 开发中（STEP-001~004 已完成，STEP-005~009 待实现） |

## 模块说明

指标体系意见征集模块是公司业务管理平台中「业务考核」一级菜单下的二级模块，负责支撑公司级年度意见征集全流程：由绩效管理员创建征集任务 → 基层单位填报意见 → 基层人资部主任审核 → 专业部门给出反馈意见 → 专业部门负责人审核 → 绩效管理员汇总发布。

## 相关菜单

| 菜单编号 | 菜单名称 | 对应页面 | 使用角色 | URL 路由 |
| --- | --- | --- | --- | --- |
| P01 | 意见征集管理 | P01 主列表/新增/编辑/查看/汇总发布页 | R01 绩效管理员 | `/opinion/survey` |
| P02 | 基层单位意见征集 | P02 列表/填报详情/查看详情 | R02 基层绩效管理员 | `/opinion/unit-fill` |
| P03 | 基层单位意见征集审核 | P03 列表/审核详情/查看详情 | R03 基层人资部主任 | `/opinion/unit-audit` |
| P04 | 专业部门反馈 | P04 列表/反馈详情/查看详情 | R04 专业绩效联络员 | `/opinion/dept-feedback` |
| P05 | 专业部门反馈审核 | P05 列表/审核详情/查看详情 | R05 专业部门负责人 | `/opinion/dept-audit` |

## 相关角色

| 角色编号 | 角色名称 | 数据权限 | 主要动作 |
| --- | --- | --- | --- |
| R01 | 绩效管理员（企管部） | 全局视图 | 创建/开启/退回/汇总/发布 |
| R02 | 基层绩效管理员 | 仅本单位 | 填报/提交/查看 |
| R03 | 基层人资部主任 | 仅本单位 | 审核通过/退回 |
| R04 | 专业绩效联络员 | 仅本部门 | 反馈/提交/查看 |
| R05 | 专业部门负责人 | 仅本部门 | 审核通过/退回 |

## 相关表

| # | 表名 | 中文名 | 用途 |
| --- | --- | --- | --- |
| 1 | `ad_opinion_survey` | 征集任务主表 | 征集基本信息与主状态 |
| 2 | `ad_opinion_survey_module` | 征集模块明细表 | 征集勾选的指标模块 |
| 3 | `ad_opinion_survey_target` | 征集对象表 | 征集涉及的基层单位 |
| 4 | `ad_opinion_unit_task` | 基层任务表 | 单位维度的填报任务 |
| 5 | `ad_opinion_item` | 基层意见行表 | 基层逐条意见 |
| 6 | `ad_opinion_unit_audit_log` | 基层审核日志表 | 审核通过/退回流水 |
| 7 | `ad_opinion_dept_task` | 专业任务表 | 部门维度的反馈任务 |
| 8 | `ad_opinion_feedback` | 专业反馈行表 | 专业部门采纳决策 |
| 9 | `ad_opinion_dept_audit_log` | 专业审核日志表 | 专业审核通过/退回流水 |
| 10 | `ad_opinion_summary_item` | 汇总采纳行表 | 绩效管理员最终采纳决策 |
| 11 | `ad_opinion_attachment` | 附件表 | 模块附件/汇总附件 |
| 12 | `ad_opinion_reminder_log` | 提醒日志表 | 一键/单条提醒流水 |
| 13 | `ad_opinion_action_log` | 业务动作审计表 | 全流程动作审计 |

## 相关接口

共 38 个接口（API-101 ~ API-902），详见 `docs/需求/需求包/2026-06-opinion-v2.1/接口清单.md`。

- 已实现：15 个（survey CRUD + unit-fill + unit-audit + attachment）
- 待实现：23 个（dept-feedback + dept-audit + progress + reminder + summary + export）

## 子模块清单

| 模块编码 | 模块名称 | 实现状态 |
| --- | --- | --- |
| `opinion.survey` | 意见征集管理 | ✅ 基础 CRUD 已实现 |
| `opinion.unit-fill` | 基层意见填报 | ✅ 已实现 |
| `opinion.unit-audit` | 基层意见审核 | ✅ 已实现 |
| `opinion.dept-feedback` | 专业部门反馈 | ❌ 待实现 |
| `opinion.dept-audit` | 专业部门反馈审核 | ❌ 待实现 |
| `opinion.progress` | 进度查询与提醒 | ❌ 待实现 |
| `opinion.summary` | 汇总发布 | ❌ 待实现 |
| `opinion.attachment` | 附件管理 | ✅ Mock 已实现 |

## 状态机

主状态共 6 个：`DRAFT` → `WAIT_FILL` → `FILLING` → `DEPT_FEEDBACK` → `DONE` → `PUBLISHED`

双层状态：主状态（ad_opinion_survey.status）+ 各阶段子状态（填报/审核子状态）

## 开发进度

| 步骤 | 内容 | 状态 |
| --- | --- | --- |
| STEP-001 | 数据库基线 | ✅ 已完成 |
| STEP-002 | 公共能力 | ✅ 已完成 |
| STEP-003 | P01 主链路 | ✅ 已完成 |
| STEP-004 | 基层链路 + 零报送 | ✅ 已完成 |
| STEP-005 | 专业链路 | ❌ 待实现 |
| STEP-006 | 进度 + 提醒 | ❌ 待实现 |
| STEP-007 | 汇总发布 | ❌ 待实现 |
| STEP-008 | 专业截止提醒 | ❌ 待实现 |
| STEP-009 | 导入/导出补完 | ❌ 待实现 |

## 关键约束

- 表前缀统一 `ad_opinion_`
- 接口前缀 `/api/opinion/`
- 状态字段统一 `VARCHAR(32)`，使用枚举常量
- 后端枚举常量为权威，前后端按三栏映射对齐
- 主状态共 6 个，禁止再增加「待发布」主状态
- 删除均为逻辑删除（`deleted_flag`）
- 发布后所有写操作禁用
