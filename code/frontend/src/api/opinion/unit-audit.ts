/**
 * 基层单位意见征集审核（P03）— Unit Audit API。
 */

import { get, post } from '@/utils/request'
import type { PageResult } from '@/types/api'
import type {
  UnitAuditDetail,
  UnitAuditListItem,
  UnitAuditListRequest,
  UnitAuditRejectRequest,
  UnitAuditStatusResult,
} from '@/types/opinion/unit-audit'

/** API-301 审核任务列表。 */
export function listAuditTasks(payload: UnitAuditListRequest) {
  return post<PageResult<UnitAuditListItem>>('/api/opinion/unit-audit/list', payload)
}

/** API-302 审核详情。 */
export function getAuditDetail(taskId: number) {
  return get<UnitAuditDetail>(`/api/opinion/unit-audit/${taskId}`)
}

/** API-303 审核通过。 */
export function passAudit(taskId: number) {
  return post<UnitAuditStatusResult>(`/api/opinion/unit-audit/${taskId}/pass`)
}

/** API-304 审核退回。 */
export function rejectAudit(taskId: number, payload: UnitAuditRejectRequest) {
  return post<UnitAuditStatusResult>(`/api/opinion/unit-audit/${taskId}/reject`, payload)
}
