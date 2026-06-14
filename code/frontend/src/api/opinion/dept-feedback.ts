/**
 * 专业部门反馈（P04）— Dept Feedback API。
 */

import { get, post } from '@/utils/request'
import type { PageResult } from '@/types/api'
import type {
  DeptFeedbackDetail,
  DeptFeedbackListItem,
  DeptFeedbackListRequest,
  DeptFeedbackSaveRequest,
  DeptFeedbackSubmitResult,
} from '@/types/opinion/dept-feedback'

/** API-401 专业反馈任务列表。 */
export function listDeptFeedbackTasks(payload: DeptFeedbackListRequest) {
  return post<PageResult<DeptFeedbackListItem>>('/api/opinion/dept-feedback/list', payload)
}

/** API-402 专业反馈任务详情。 */
export function getDeptFeedbackDetail(taskId: number) {
  return get<DeptFeedbackDetail>(`/api/opinion/dept-feedback/${taskId}`)
}

/** API-403 保存专业反馈草稿。 */
export function saveDeptFeedback(taskId: number, payload: DeptFeedbackSaveRequest) {
  return post<void>(`/api/opinion/dept-feedback/${taskId}/save`, payload)
}

/** API-404 提交专业反馈。 */
export function submitDeptFeedback(taskId: number) {
  return post<DeptFeedbackSubmitResult>(`/api/opinion/dept-feedback/${taskId}/submit`)
}
