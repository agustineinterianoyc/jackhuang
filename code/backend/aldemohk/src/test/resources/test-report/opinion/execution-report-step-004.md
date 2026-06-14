# 测试执行说明 — STEP-004 基层链路

## 功能点

- API-201：基层任务列表查询（按单位隔离、年份/名称/填报状态筛选、分页）
- API-202：基层任务详情（含模块、附件、意见行；PUBLISHED 时透出调整后内容）
- API-203：保存基层意见（PENDING 可写、整批替换、先删后插）
- API-204：提交基层意见（空意见截止前阻断、WAIT_FILL→FILLING 自动推进、状态更新）
- API-301：基层审核列表（仅 SUBMITTED 任务、FILLING 状态过滤、按单位隔离）
- API-302：基层审核详情（含模块、意见行、审核日志）
- API-303：审核通过（PENDING→PASS、写入审计日志）
- API-304：审核退回（退回原因必填、fillStatus→PENDING、auditStatus→REJECTED）
- API-110：绩效退回基层（R01 在 FILLING 主状态时退回单个基层）
- 零报送 Job：每 5 分钟扫描过期 PENDING 任务自动提交+审核通过

## 相关代码文件

- `com.hk.demo.app.controller.opinion.unitfill.OpinionUnitFillController`
- `com.hk.demo.app.service.opinion.unitfill.OpinionUnitFillService` / `impl.OpinionUnitFillServiceImpl`
- `com.hk.demo.app.controller.opinion.unitaudit.OpinionUnitAuditController`
- `com.hk.demo.app.service.opinion.unitaudit.OpinionUnitAuditService` / `impl.OpinionUnitAuditServiceImpl`
- `com.hk.demo.app.task.opinion.OpinionAutoZeroReportJob`
- `com.hk.demo.app.service.opinion.survey.OpinionSurveyService` / `impl.OpinionSurveyServiceImpl`（新增 opsRejectUnit）
- 前端：`views/opinion/unit-fill/{List,Detail}.vue`、`views/opinion/unit-audit/{List,Detail}.vue`
- 前端：`api/opinion/unit-fill.ts`、`api/opinion/unit-audit.ts`
- 前端：`types/opinion/unit-fill.ts`、`types/opinion/unit-audit.ts`
- 前端：`constants/opinion.ts`（新增填报状态/审核状态常量）
- 前端：`router/index.ts`（新增 4 条路由）

## 测试文件

- 存量：`com.hk.demo.app.opinion.state.OpinionStateMachineTest`（11 个用例，全部通过）
- 新增测试：暂无（待后续补充）

## 执行命令

```bash
# 后端编译
cd code/backend
./mvnw -pl aldemohk -am compile -DskipTests

# 后端存量测试
./mvnw test -pl aldemohk -am -Dtest=com.hk.demo.app.opinion.state.OpinionStateMachineTest

# 前端类型检查
cd code/frontend
npm run type-check

# 前端构建
npm run build
```

## 测试结果

- 后端：mvn compile BUILD SUCCESS
- 后端：OpinionStateMachineTest 11/11 通过
- 前端：vue-tsc type-check 通过
- 前端：vite build 通过（产物正常输出）

## 关键断言点

- 状态机 WAIT_FILL → FILLING 自动推进：detail() 和 submit() 中均可触发
- save() 仅 PENDING 状态可写，SUBMITTED 时抛 OPINION_ILLEGAL_STATE
- submit() 空意见 + 截止前阻止（OPINION_EMPTY_NOT_ALLOWED）
- audit pass 要求 auditStatus=PENDING + survey=FILLING
- audit reject 要求退回原因非空（OPINION_REJECT_REASON_REQUIRED）
- opsRejectUnit 要求主状态=FILLING + auditStatus=PENDING

## 未覆盖项

- Controller 层集成测试（需启动 Spring 上下文 + MySQL，当前不可用）
- Service 层单元测试（unit-fill / unit-audit 各场景）
- 零报送 Job 定时触发测试（需真实调度环境）
- 前端 E2E 测试
- 角色越权测试（需完整权限上下文）

## 剩余风险

- 零报送扫描效率：当前一次性加载全表 PENDING 任务，数据量大时需加分页
- mock 数据范围：R02/R03 当前固定为 unitId=101L，真实环境需从安全上下文读取
- API 幂等性：提交/审核接口对重复调用未做幂等保护

## 备注

- 所有新增代码落在 `code/backend/aldemohk/` 和 `code/frontend/` 正确子目录
- 代码遵循已有分层规则：controller → service → mapper
- 前端组件复用 OPINION_PAGE_SIZE_OPTIONS 和常量映射
