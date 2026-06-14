/**
 * 基层单位意见征集（P02）— Unit Fill API。
 */

import { get, post } from '@/utils/request'
import type { PageResult } from '@/types/api'
import type {
  UnitFillDetail,
  UnitFillListItem,
  UnitFillListRequest,
  UnitFillSaveRequest,
  UnitFillSubmitResult,
} from '@/types/opinion/unit-fill'

/** API-201 基层任务列表。 */
export function listUnitTasks(payload: UnitFillListRequest) {
  return post<PageResult<UnitFillListItem>>('/api/opinion/unit-fill/list', payload)
}

/** API-202 基层任务详情。 */
export function getUnitTaskDetail(taskId: number) {
  return get<UnitFillDetail>(`/api/opinion/unit-fill/${taskId}`)
}

/** API-203 保存基层意见。 */
export function saveUnitOpinion(taskId: number, payload: UnitFillSaveRequest) {
  return post<void>(`/api/opinion/unit-fill/${taskId}/save`, payload)
}

/** API-204 提交基层意见。 */
export function submitUnitOpinion(taskId: number) {
  return post<UnitFillSubmitResult>(`/api/opinion/unit-fill/${taskId}/submit`)
}
