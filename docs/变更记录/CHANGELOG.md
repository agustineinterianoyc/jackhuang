# CHANGELOG

[项目初始化前-20260614105111/](../历史归档/项目初始化前-20260614105111/CHANGELOG.md)

## 2026-06-14 — Bug 修复（提交 e9a6ff4, bf0bc23, ed01d2b）

### Bug 修复

| Bug | 影响 | 根因 | 修复 | 提交 |
|-----|------|------|------|------|
| 专业审核详情页 500 | P05 页面不可用 | `detail()` 限制仅 PENDING 可查看 | 移除状态检查，所有状态均可查看 | `e9a6ff4` |
| 专业审核列表状态显示空白 | 提交状态/审核状态不显示 | `DeptAuditListItemVO` 缺少 `*Text` 字段 | 补充 `submitStatusText`/`auditStatusText` | `bf0bc23` |
| 专业反馈保存 500 | 反馈数据无法持久化 | 多次保存同一 dept_task 时唯一键冲突 | `INSERT ON DUPLICATE KEY UPDATE` | `ed01d2b` |
| Feedback 列表查询空返回 | 审核详情页无反馈数据 | MyBatis-Plus `selectList` 在 Boot4 下返回空 | FeedbackRepository 改用 JDBC 直连 | `e9a6ff4` |

### 功能变更

| 变更 | 描述 | 提交 |
|------|------|------|
| 专业反馈行增加备注 | `ad_opinion_feedback` 新增 `remark VARCHAR(500)` | `ed01d2b` |
| STEP-005 全栈开发 | P04+P05 后端 10 个 API + 前端 4 个页面 | `9ff5b66` `7305511` |
| STEP-006 进度/提醒 | API-601~604 进度查询+提醒 | `334d7d9` |
| 数据隔离修复 | 移除 unit-audit/dept-feedback/dept-audit 硬编码隔离 | `334d7d9` |

- **影响模块**：`opinion`
- **验证结果**：17/17 功能测试通过，全流程 E2E 通过
