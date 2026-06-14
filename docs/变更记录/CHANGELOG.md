# CHANGELOG

[项目初始化前-20260614105111/](../历史归档/项目初始化前-20260614105111/CHANGELOG.md)

> **详细需求变更记录**：参见 [需求变更.md](需求变更.md)

## 2026-06-14 (latest) — STEP-007/008/009 + 全面测试（提交 f6a43ab, 10964cc）

- **变更类型**：功能完成 + 测试
- **新增 API**：summarize(106)、publish(107)、summary CRUD(701~704)、export(801~802)
- **新增 Job**：OpinionDeptDeadlineRemindJob
- **前端新增**：ProgressModal、Summary.vue、4个导出按钮
- **测试**：21/21 通过，85+条数据覆盖12张表

## 2026-06-14 — Bug修复 + remark字段 + STEP-006（提交 e9a6ff4, bf0bc23, ed01d2b, 334d7d9）

### Bug 修复
| Bug | 根因 | 修复 |
|-----|------|------|
| P05 详情页500 | detail() 限制仅PENDING可查看 | 移除状态检查 |
| P05 列表状态空白 | VO缺*Text字段 | 补充submitStatusText/auditStatusText |
| 反馈保存500 | 多次保存唯一键冲突 | JDBC ON DUPLICATE KEY UPDATE |
| selectList空返回 | MyBatis-Plus Boot4兼容 | JDBC绕行 |

### 功能变更
- STEP-005 全栈：P04+P05 后端10 API + 前端4页面
- STEP-006 后端：API-601~604 进度+提醒
- 需求变更：ad_opinion_feedback 新增 remark VARCHAR(500)

## 2026-06-14 — 项目初始化 + 设计基线 + 需求包

- 项目重初始化为 aldemohk
- 5份设计文档生成（设计+数据库+接口+实现计划）
- 需求包 2026-06-opinion-v2.1 生成
