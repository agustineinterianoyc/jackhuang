/**
 * 基层单位意见征集（P02）— 类型定义。
 */

/** API-201 列表查询入参。 */
export interface UnitFillListRequest {
  assessYear?: number | null
  name?: string | null
  fillStatus?: string[] | null
  page: number
  pageSize: number
}

/** API-201 列表行。 */
export interface UnitFillListItem {
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
  unitDeadline: string
  submittedAt: string | null
}

/** API-202 详情 — 任务摘要。 */
export interface UnitFillTask {
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
}

/** API-202 详情 — 模块附件。 */
export interface UnitFillAttachment {
  fileId: string
  fileName: string
}

/** API-202 详情 — 模块。 */
export interface UnitFillModule {
  moduleCode: string
  moduleName: string
  attachments: UnitFillAttachment[]
}

/** API-202/203 意见行。 */
export interface UnitFillItem {
  id?: number
  moduleCode: string
  indicatorCategory?: string | null
  indicatorName?: string | null
  factorName?: string | null
  extraField?: string | null
  opinionCategory?: string | null
  opinionContent?: string | null
  reason?: string | null
  displayOrder?: number
}

/** API-202 详情响应。 */
export interface UnitFillDetail {
  task: UnitFillTask
  modules: UnitFillModule[]
  items: UnitFillItem[]
  adjustedItems?: UnitFillItem[] | null
}

/** API-203 保存入参。 */
export interface UnitFillSaveRequest {
  items: UnitFillItem[]
}

/** API-204 提交响应。 */
export interface UnitFillSubmitResult {
  taskId: number
  fillStatus: string
  auditStatus: string
}
