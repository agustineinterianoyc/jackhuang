/**
 * 专业部门反馈（P04）— 类型定义。
 */

/** API-401 列表查询入参。 */
export interface DeptFeedbackListRequest {
  assessYear?: number | null
  name?: string | null
  submitStatus?: string[] | null
  page: number
  pageSize: number
}

/** API-401 列表行。 */
export interface DeptFeedbackListItem {
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

/** API-402 详情 — 任务信息。 */
export interface DeptFeedbackTask {
  id: number
  surveyId: number
  surveyName: string
  assessYear: number
  surveyStatus: string
  surveyStatusText: string
  submitStatus: string
  submitStatusText: string
  auditStatus: string
  auditStatusText: string
  deptDeadline: string
  submittedAt: string | null
}

/** API-402 详情 — 模块。 */
export interface DeptFeedbackModule {
  moduleCode: string
  moduleName: string
  attachments: { fileId: string; fileName: string }[]
}

/** API-402 详情 — 反馈意见行（含基层意见信息 + 反馈字段）。 */
export interface DeptFeedbackItem {
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
}

/** API-402 详情响应。 */
export interface DeptFeedbackDetail {
  task: DeptFeedbackTask
  modules: DeptFeedbackModule[]
  items: DeptFeedbackItem[]
}

/** API-403 保存入参 — 单条反馈。 */
export interface DeptFeedbackSaveItem {
  itemId: number
  isAdopted: boolean
  adoptionRemark?: string | null
}

/** API-403 保存入参。 */
export interface DeptFeedbackSaveRequest {
  items: DeptFeedbackSaveItem[]
}

/** API-404 提交响应。 */
export interface DeptFeedbackSubmitResult {
  taskId: number
  submitStatus: string
  submitStatusText: string
}
