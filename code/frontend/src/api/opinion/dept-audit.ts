/**
 * 专业部门反馈审核（P05）— Dept Audit API。
 */

import { get, post } from '@/utils/request'
import type { PageResult } from '@/types/api'
import type {
  DeptAuditDetail,
  DeptAuditListItem,
  DeptAuditListRequest,
  DeptAuditRejectRequest,
  DeptAuditStatusResult,
} from '@/types/opinion/dept-audit'

/** API-501 审核任务列表。 */
export function listDeptAuditTasks(payload: DeptAuditListRequest) {
  return post<PageResult<DeptAuditListItem>>('/api/opinion/dept-audit/list', payload)
}

/** API-502 审核详情。 */
export function getDeptAuditDetail(taskId: number) {
  return get<DeptAuditDetail>(`/api/opinion/dept-audit/${taskId}`)
}

/** API-503 审核通过。 */
export function passDeptAudit(taskId: number) {
  return post<DeptAuditStatusResult>(`/api/opinion/dept-audit/${taskId}/pass`)
}

/** API-504 审核退回。 */
export function rejectDeptAudit(taskId: number, payload: DeptAuditRejectRequest) {
  return post<DeptAuditStatusResult>(`/api/opinion/dept-audit/${taskId}/reject`, payload)
}
