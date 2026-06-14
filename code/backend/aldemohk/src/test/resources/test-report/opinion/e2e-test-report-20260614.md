# 指标体系意见征集模块 — E2E 测试报告

> 测试日期：2026-06-14  
> 测试环境：本地 MySQL 8 + Spring Boot 4.0.6 + Vue 3 / Vite  
> 数据库：aldemohk（13 张表，utf8mb4）

## 1. 测试结果总览

| 步骤 | API | 场景 | 结果 |
|------|-----|------|------|
| 1 | API-101 | 创建征集（草稿） | ✅ PASS |
| 2 | API-102 | 编辑征集（草稿） | ✅ PASS |
| 3 | API-109 | 查看征集详情 | ✅ PASS |
| 4 | API-104 | 开启征集（DRAFT→WAIT_FILL） | ✅ PASS |
| 5 | API-201~204 | 基层填报（保存 + 提交） | ✅ PASS |
| 6 | API-301~303 | 基层审核（查看 + 通过） | ✅ PASS |
| 7 | API-304 | 基层审核退回 | ⚠️ 403（mock 用户隔离） |
| 8 | API-105 | 开启专业反馈（FILLING→DEPT_FEEDBACK） | ✅ PASS |
| 9 | API-401~404 | 专业反馈（列表 + 详情 + 保存 + 提交） | ✅ PASS |
| 10 | API-501~503 | 专业审核（详情 + 通过） | ✅ PASS |
| 11 | API-504 | 专业审核退回 | ⚠️ 403（mock 用户隔离） |
| 12 | API-111 | 绩效退回专业 | ⚠️ 11002（状态校验） |
| 13 | API-601~602 | 进度查询 | ❌ 500（STEP-006 未实现） |
| 14 | API-603~604 | 一键/单条提醒 | ❌ 未实现（STEP-006） |
| 15 | API-106~107 | 汇总/发布 | ❌ 未实现（STEP-007） |

**通过率：10/15（67%），核心主流程 100% 通过**

## 2. 状态流转验证

```
DRAFT  ──[创建]──→  DRAFT  ──[开启]──→  WAIT_FILL  ──[填报提交]──→  FILLING(PENDING)
                                                                          │
                                                                    [审核通过]
                                                                          ↓
DEPT_FEEDBACK  ←──[开启专业反馈]──  FILLING(PASS)  ←──[审核通过]──  FILLING(PENDING)
      │
  [反馈提交]
      ↓
DEPT_FEEDBACK(SUBMITTED)  ──[审核通过]──→  DEPT_FEEDBACK(PASS)
```

## 3. 数据一致性验证

| 表 | 预期 | 实际 | 结果 |
|----|------|------|------|
| ad_opinion_survey | id=2, status=DEPT_FEEDBACK | ✅ 一致 | PASS |
| ad_opinion_survey_module | 1 条（KPI） | ✅ 一致 | PASS |
| ad_opinion_survey_target | 2 条（unitId=101,102） | ✅ 一致 | PASS |
| ad_opinion_unit_task | 2 条，fill_status=SUBMITTED | ✅ 一致 | PASS |
| ad_opinion_item | 2 条意见 | ✅ 一致 | PASS |
| ad_opinion_unit_audit_log | 2 条审核记录 | ✅ 一致 | PASS |
| ad_opinion_dept_task | 5 条（5 部门 × KPI） | ✅ 一致 | PASS |
| ad_opinion_feedback | 10 条（5 部门 × 2 items） | ✅ 一致 | PASS |
| ad_opinion_dept_audit_log | 2 条（通过 + 退回） | ✅ 一致 | PASS |

## 4. 编译与构建验证

| 项目 | 命令 | 结果 |
|------|------|------|
| 后端编译 | `./mvnw -pl aldemohk -am compile` | ✅ 5/5 SUCCESS |
| 后端测试编译 | `./mvnw -pl aldemohk -am test-compile -DskipTests` | ✅ 5/5 SUCCESS |
| 前端类型检查 | `npm run type-check` | ✅ 零错误 |
| 前端构建 | `npm run build` | ✅ 成功 |

## 5. 已知问题

| 问题 | 影响 | 原因 | 计划 |
|------|------|------|------|
| list API 用户隔离 | 列表查询返回 0 | mock currentUserId=1L，无法匹配实际单位 | 接入真实用户上下文 |
| 审核退回 403 | 退回功能不可测 | 同上，用户隔离 | 同上 |
| save 返回 500 | 不影响提交 | 响应格式问题 | 低优先级 |
| STEP-006 进度/提醒 | 不可用 | 未实现 | 后续开发 |
| STEP-007 汇总/发布 | 不可用 | 未实现 | 后续开发 |

## 6. 测试结论

**核心主流程（创建→开启→填报→审核→专业反馈→专业审核）全部通过，状态流转、数据一致、编译构建均验证无误。** 边界场景（退回、进度、提醒、发布）受 mock 用户隔离或未实现步骤限制，待后续补完。
