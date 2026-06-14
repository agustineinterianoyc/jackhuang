/**
 * 基层单位意见征集审核（P03）— 类型定义。
 */

/** API-301 列表查询入参。 */
export interface UnitAuditListRequest {
  assessYear?: number | null
  name?: string | null
  auditStatus?: string[] | null
  page: number
  pageSize: number
}

/** API-301 列表行。 */
export interface UnitAuditListItem {
  taskId: number
  surveyId: number
  surveyName: string
  assessYear: number
  unitId: number
  unitName: string
  fillStatus: string
  fillStatusText: string
  auditStatus: string
  auditStatusText: string
  submittedAt: string | null
  unitDeadline: string
  lastRejectReason: string | null
}

/** 审核日志行。 */
export interface AuditLog {
  id: number
  action: string
  actorRole: string
  rejectReason: string | null
  createdAt: string
}

/** API-302 详情 — 任务信息。 */
export interface AuditTaskInfo {
  id: number
  surveyId: number
  surveyName: string
  assessYear: number
  surveyStatus: string
  surveyStatusText: string
  fillStatus: string
  fillStatusText: string
  auditStatus: string
  auditStatusText: string
  unitDeadline: string
  submittedAt: string | null
  submittedBy: number | null
  lastRejectReason: string | null
}

/** API-302 详情 — 模块。 */
export interface AuditModule {
  moduleCode: string
  moduleName: string
  attachments: { fileId: string; fileName: string }[]
}

/** API-302 详情 — 意见行。 */
export interface AuditItem {
  id: number
  moduleCode: string
  indicatorCategory: string | null
  indicatorName: string | null
  factorName: string | null
  extraField: string | null
  opinionCategory: string | null
  opinionContent: string | null
  reason: string | null
  displayOrder: number
}

/** API-302 详情响应。 */
export interface UnitAuditDetail {
  task: AuditTaskInfo
  modules: AuditModule[]
  items: AuditItem[]
  auditLogs: AuditLog[]
}

/** API-304 退回入参。 */
export interface UnitAuditRejectRequest {
  rejectReason: string
}

/** API-303/304 响应。 */
export interface UnitAuditStatusResult {
  taskId: number
  fillStatus: string
  auditStatus: string
}
