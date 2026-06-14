# Bug 修复记录

## 2026-06-14 — 指标体系意见征集模块

### Bug #1: 专业反馈保存 500 (API-403)

- **发现时间**: 2026-06-14
- **影响**: 专业绩效联络员无法保存反馈意见
- **根因**: `OpinionFeedbackRepository.batchSaveOrUpdate()` 使用 `mapper.delete()` + `mapper.insert()`，而 `@TableLogic` 导致 `delete()` 执行逻辑删除（SET deleted_flag=1），后续 `insert()` 与唯一键 `(dept_task_id, item_id, deleted_flag=0)` 冲突
- **修复**: 改用 JDBC `INSERT ON DUPLICATE KEY UPDATE`，先查后插/更新的方式
- **提交**: `ed01d2b`

### Bug #2: MyBatis-Plus selectList 返回空

- **发现时间**: 2026-06-14
- **影响**: 所有列表查询接口返回空数据
- **根因**: MyBatis-Plus 3.5.15 本身 Bug，与 Spring Boot 版本无关
- **已尝试方案**:
  - ❌ 升级 3.5.17（Maven Central 不存在）
  - ❌ 升级 3.5.16（同版本 Bug，未修复）
  - ❌ @InterceptorIgnore 注解（与拦截器无关，无效）
  - ❌ 降级 Spring Boot 4→3.4.5（Bug 在 MP 本身，Boot 版本无关）
- **临时规避**: FeedbackRepository、SurveyServiceImpl.summarize()、SummaryServiceImpl 改用 JDBC 直连
- **推荐方案**: 等待 MyBatis-Plus 3.5.17+ 发布，或将列表查询统一迁移为 MyBatis XML / JdbcTemplate
- **状态**: ⏳ 已定位，部分 JDBC 绕行

### Bug #3: 专业审核详情页 500

- **发现时间**: 2026-06-14
- **影响**: 已审核通过的专业任务无法查看详情
- **根因**: `OpinionDeptAuditServiceImpl.detail()` 方法有限制 `if (!PENDING) throw exception`，非待审状态无法查看
- **修复**: 移除状态限制，所有状态均可查看详情
- **提交**: `e9a6ff4`

### Bug #4: 专业审核列表状态字段显示空白

- **发现时间**: 2026-06-14
- **影响**: P05 列表页提交状态和审核状态显示为空
- **根因**: `DeptAuditListItemVO` 缺少 `submitStatusText` 和 `auditStatusText` 字段，前端依赖这两个字段显示
- **修复**: VO 补充 text 字段，Service 中赋值
- **提交**: `bf0bc23`
