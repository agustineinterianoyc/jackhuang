/**
 * 指标体系意见征集模块 — 类型定义。
 */

import type { OpinionMainStatus, OpinionModuleCode, OpinionUnitType } from '@/constants/opinion'

/** API-108 列表查询入参。 */
export interface SurveyListRequest {
  assessYear?: number | null
  name?: string | null
  status?: OpinionMainStatus[] | null
  page: number
  pageSize: number
}

/** API-108 列表行。 */
export interface SurveyListItem {
  id: number
  name: string
  assessYear: number
  status: OpinionMainStatus
  statusText: string
  unitDeadline: string
  deptDeadline: string
  createdAt: string
  unitProgress: string
  deptProgress: string
}

/** 字典：单位项。 */
export interface DictUnit {
  unitId: number
  unitName: string
  unitType: OpinionUnitType
  unitTypeText: string
}

/** 字典：通用枚举项。 */
export interface DictEnum {
  code: string
  text: string
}

/** 模块附件。 */
export interface ModuleAttachment {
  id?: number
  fileId: string
  fileName: string
  fileSize?: number
}

/** 创建/编辑入参 — 征集对象。 */
export interface SurveyTarget {
  unitId: number
}

/** 创建/编辑入参 — 征集模块。 */
export interface SurveyModule {
  moduleCode: OpinionModuleCode
  moduleAlias?: string | null
  displayOrder?: number
  attachments?: ModuleAttachment[]
}

/** API-101 / API-102 入参。 */
export interface SurveySaveRequest {
  name: string
  assessYear: number
  noticeContent?: string | null
  remark?: string | null
  unitDeadline: string
  deptDeadline: string
  targets: SurveyTarget[]
  modules: SurveyModule[]
}

/** 状态推进类响应。 */
export interface SurveyStatusResult {
  id: number
  status: OpinionMainStatus
  statusText: string
  startAt?: string | null
  publishAt?: string | null
}

/** API-104 开启征集响应。 */
export interface SurveyStartResult extends SurveyStatusResult {
  unitTaskCount: number
}

/** API-109 详情。 */
export interface SurveyDetail {
  id: number
  name: string
  assessYear: number
  noticeContent?: string | null
  remark?: string | null
  unitDeadline: string
  deptDeadline: string
  status: OpinionMainStatus
  statusText: string
  startAt?: string | null
  publishAt?: string | null
  createdAt: string
  updatedAt: string
  targets: DetailTarget[]
  modules: DetailModule[]
}

export interface DetailTarget {
  unitId: number
  unitName: string
  unitType: OpinionUnitType
  unitTypeText: string
}

export interface DetailModule {
  id: number
  moduleCode: OpinionModuleCode
  moduleName: string
  moduleAlias?: string | null
  displayOrder: number
  attachments: DetailAttachment[]
}

export interface DetailAttachment {
  id: number
  fileId: string
  fileName: string
  fileSize: number
}

/** 附件上传响应。 */
export interface UploadResult {
  fileId: string
  fileName: string
  fileSize: number
}

/** 状态推进响应（与 SurveyStatusResult 对齐）。 */
export interface SurveyStatus {
  id: number
  status: string
  statusText: string
  startAt: string | null
  publishAt: string | null
}

/** 单位进度 VO。 */
export interface UnitProgressVO {
  taskId: number
  unitId: number
  unitName: string
  unitType: string
  unitTypeText: string
  fillStatus: string
  fillStatusText: string
  auditStatus: string
  auditStatusText: string
  submittedAt: string | null
}

/** 专业部门进度 VO。 */
export interface DeptProgressVO {
  taskId: number
  departmentId: number
  departmentName: string
  moduleCode: string
  moduleName: string
  submitStatus: string
  submitStatusText: string
  auditStatus: string
  auditStatusText: string
}
