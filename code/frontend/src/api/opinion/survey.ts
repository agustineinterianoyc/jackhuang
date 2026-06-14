/**
 * 意见征集管理（P01）— Survey API（API-101 ～ API-109）。
 */

import { del, get, post, put } from '@/utils/request'
import type { PageResult } from '@/types/api'
import type {
  DeptProgressVO,
  DictEnum,
  DictUnit,
  SurveyDetail,
  SurveyListItem,
  SurveyListRequest,
  SurveySaveRequest,
  SurveyStartResult,
  SurveyStatusResult,
  UnitProgressVO,
  UploadResult,
} from '@/types/opinion/survey'

/** API-101 创建征集（草稿）。 */
export function createSurvey(payload: SurveySaveRequest) {
  return post<SurveyStatusResult>('/api/opinion/survey', payload)
}

/** API-102 编辑征集（草稿）。 */
export function updateSurvey(id: number, payload: SurveySaveRequest) {
  return put<SurveyStatusResult>(`/api/opinion/survey/${id}`, payload)
}

/** API-103 删除征集（草稿）。 */
export function deleteSurvey(id: number) {
  return del<void>(`/api/opinion/survey/${id}`)
}

/** API-104 开启征集。 */
export function startSurvey(id: number) {
  return post<SurveyStartResult>(`/api/opinion/survey/${id}/start`)
}

/** API-108 列表查询。 */
export function listSurveys(payload: SurveyListRequest) {
  return post<PageResult<SurveyListItem>>('/api/opinion/survey/list', payload)
}

/** API-109 详情。 */
export function getSurveyDetail(id: number) {
  return get<SurveyDetail>(`/api/opinion/survey/${id}`)
}

/** 字典：单位列表。 */
export function dictListUnits() {
  return get<DictUnit[]>('/api/opinion/dict/units')
}

/** 字典：模块列表。 */
export function dictListModules() {
  return get<DictEnum[]>('/api/opinion/dict/modules')
}

/** 附件上传（mock）。 */
export function uploadAttachment(formData: FormData) {
  return post<UploadResult>('/api/opinion/attachment/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

/** API-106 汇总征集。 */
export function summarizeSurvey(id: number) {
  return post<SurveyStatusResult>(`/api/opinion/survey/${id}/summarize`)
}

/** API-107 发布征集。 */
export function publishSurvey(id: number) {
  return post<SurveyStatusResult>(`/api/opinion/survey/${id}/publish`)
}

/** API-501 单位填报进度查询。 */
export function queryUnitProgress(
  surveyId: number,
  params: { keyword?: string; submitStatus?: string; page: number; pageSize: number },
) {
  return post<PageResult<UnitProgressVO>>('/api/opinion/progress/units', { surveyId, ...params })
}

/** API-502 专业部门进度查询。 */
export function queryDeptProgress(
  surveyId: number,
  params: { keyword?: string; submitStatus?: string; page: number; pageSize: number },
) {
  return post<PageResult<DeptProgressVO>>('/api/opinion/progress/depts', { surveyId, ...params })
}

/** API-601 一键提醒。 */
export function batchRemind(surveyId: number, targetType: string) {
  return post('/api/opinion/reminder/batch', { surveyId, targetType })
}

/** API-602 单个提醒。 */
export function singleRemind(surveyId: number, targetType: string, targetId: number) {
  return post('/api/opinion/reminder/single', { surveyId, targetType, targetId })
}
