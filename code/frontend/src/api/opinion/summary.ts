/**
 * 意见征集汇总发布 — Summary API（API-701 ～ API-702）。
 */

import { get, post } from '@/utils/request'
import type { PageResult } from '@/types/api'

export interface SummaryItemVO {
  id: number
  moduleCode: string
  departmentName: string
  indicatorCategory: string
  indicatorName: string
  factorName: string
  unitName: string
  opinionCategory: string
  opinionContent: string
  isAdopted: boolean
  adoptionRemark: string
  finalIsAdopted: boolean
  adjustedContent?: string
}

export function listSummaryItems(
  surveyId: number,
  params: { moduleCode?: string; page: number; pageSize: number },
) {
  return post<PageResult<SummaryItemVO>>(`/api/opinion/summary/${surveyId}/items`, params)
}

export function saveSummaryItems(surveyId: number, items: any[]) {
  return post(`/api/opinion/summary/${surveyId}/save`, { items })
}
