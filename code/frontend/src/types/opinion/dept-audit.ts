/**
 * 专业部门反馈审核（P05）— 类型定义。
 */

/** API-501 列表查询入参。 */
export interface DeptAuditListRequest {
  assessYear?: number | null
  name?: string | null
  auditStatus?: string[] | null
  page: number
  pageSize: number
}

/** API-501 列表行。 */
export interface DeptAuditListItem {
  taskId: number
  surveyId: number
  surveyName: string
  assessYear: number
  moduleCode: string
  moduleName: string
  deptDeadline: string
  submitStatus: string
  submitStatusText: string
  auditStatus: string
  auditStatusText: string
  surveyStatus: string
  surveyStatusText: string
}

/** 专业审核日志行。 */
export interface DeptAuditLog {
  id: number
  action: string
  actorRole: string
  rejectReason: string | null
  createdAt: string
}

/** API-502 详情 — 任务信息。 */
export interface DeptAuditTask {
  id: number
  surveyId: number
  surveyName: string
  assessYear: number
  surveyStatus: string
  submitStatus: string
  submitStatusText: string
  auditStatus: string
  auditStatusText: string
  deptDeadline: string
  submittedAt: string | null
  lastRejectReason: string | null
}

/** API-502 详情 — 模块。 */
export interface DeptAuditModule {
  moduleCode: string
  moduleName: string
  attachments: { fileId: string; fileName: string }[]
}

/** API-502 详情 — 反馈意见行（含基层意见 + 反馈 + 审核信息）。 */
export interface DeptAuditItem {
  itemId: number
  moduleCode: string
  indicatorCategory: string | null
  indicatorName: string | null
  factorName: string | null
  unitName: string
  opinionCategory: string | null
  opinionContent: string | null
  reason: string | null
  isAdopted: boolean | null
  adoptionRemark: string | null
  remark: string | null
}

/** API-502 详情响应。 */
export interface DeptAuditDetail {
  task: DeptAuditTask
  modules: DeptAuditModule[]
  items: DeptAuditItem[]
  auditLogs: DeptAuditLog[]
}

/** API-504 退回入参。 */
export interface DeptAuditRejectRequest {
  rejectReason: string
}

/** API-503/504 响应。 */
export interface DeptAuditStatusResult {
  taskId: number
  submitStatus: string
  auditStatus: string
}
