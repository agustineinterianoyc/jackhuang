/**
 * 意见征集管理（P01）— Survey API（API-101 ～ API-109）。
 */

import { del, get, post, put } from '@/utils/request'
import type { PageResult } from '@/types/api'
import type {
  DictEnum,
  DictUnit,
  SurveyDetail,
  SurveyListItem,
  SurveyListRequest,
  SurveySaveRequest,
  SurveyStartResult,
  SurveyStatusResult,
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
