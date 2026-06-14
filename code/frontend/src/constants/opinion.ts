/**
 * 指标体系意见征集模块 — 三栏映射常量
 *
 * 后端常量是权威；前端按本文件做请求与展示。
 *
 * 与设计文档 §7 保持一致：
 * - docs/设计/指标体系意见征集模块/数据库设计说明书 指标体系意见征集模块.md
 */

/** 主状态枚举（共 6 个）。 */
export const OPINION_MAIN_STATUS = {
  DRAFT: 'DRAFT',
  WAIT_FILL: 'WAIT_FILL',
  FILLING: 'FILLING',
  DEPT_FEEDBACK: 'DEPT_FEEDBACK',
  DONE: 'DONE',
  PUBLISHED: 'PUBLISHED',
} as const

export type OpinionMainStatus = (typeof OPINION_MAIN_STATUS)[keyof typeof OPINION_MAIN_STATUS]

/** 主状态中文映射。 */
export const OPINION_MAIN_STATUS_TEXT: Record<OpinionMainStatus, string> = {
  DRAFT: '草稿',
  WAIT_FILL: '待填报',
  FILLING: '填报中',
  DEPT_FEEDBACK: '专业反馈中',
  DONE: '已完成',
  PUBLISHED: '已发布',
}

/** 状态颜色（用于 a-tag color 属性）。 */
export const OPINION_MAIN_STATUS_COLOR: Record<OpinionMainStatus, string> = {
  DRAFT: 'gray',
  WAIT_FILL: 'arcoblue',
  FILLING: 'orange',
  DEPT_FEEDBACK: 'orangered',
  DONE: 'cyan',
  PUBLISHED: 'green',
}

/** 5 个指标 Tab 模块编码。 */
export const OPINION_MODULE_CODE = {
  KPI: 'KPI',
  BONUS: 'BONUS',
  PARTY: 'PARTY',
  SAFETY: 'SAFETY',
  OTHER: 'OTHER',
} as const

export type OpinionModuleCode = (typeof OPINION_MODULE_CODE)[keyof typeof OPINION_MODULE_CODE]

export const OPINION_MODULE_CODE_TEXT: Record<OpinionModuleCode, string> = {
  KPI: '关键业绩指标',
  BONUS: '业绩争取加分',
  PARTY: '党建工作指标',
  SAFETY: '安全工作指标',
  OTHER: '其他',
}

export const OPINION_MODULE_OPTIONS: { code: OpinionModuleCode; text: string }[] = (
  Object.keys(OPINION_MODULE_CODE_TEXT) as OpinionModuleCode[]
).map((code) => ({ code, text: OPINION_MODULE_CODE_TEXT[code] }))

/** 单位类型。 */
export const OPINION_UNIT_TYPE = {
  POWER: 'POWER',
  SUPPORT: 'SUPPORT',
  MARKET: 'MARKET',
  OTHER: 'OTHER',
} as const

export type OpinionUnitType = (typeof OPINION_UNIT_TYPE)[keyof typeof OPINION_UNIT_TYPE]

export const OPINION_UNIT_TYPE_TEXT: Record<OpinionUnitType, string> = {
  POWER: '供电单位',
  SUPPORT: '业务支撑单位',
  MARKET: '市场化单位',
  OTHER: '其他单位',
}

/** 列表筛选可选状态（与原型 P01 列表保持一致）。 */
export const OPINION_LIST_STATUS_OPTIONS: { value: OpinionMainStatus; label: string }[] = [
  { value: 'DRAFT', label: '草稿' },
  { value: 'WAIT_FILL', label: '待填报' },
  { value: 'FILLING', label: '填报中' },
  { value: 'DEPT_FEEDBACK', label: '专业反馈中' },
  { value: 'DONE', label: '已完成' },
  { value: 'PUBLISHED', label: '已发布' },
]

/** 分页大小可选项（10/20/50/100）。 */
export const OPINION_PAGE_SIZE_OPTIONS = [10, 20, 50, 100]

export function statusText(status?: string | null): string {
  if (!status) return '-'
  return OPINION_MAIN_STATUS_TEXT[status as OpinionMainStatus] ?? status
}

export function statusColor(status?: string | null): string {
  if (!status) return 'gray'
  return OPINION_MAIN_STATUS_COLOR[status as OpinionMainStatus] ?? 'gray'
}

/** 基层填报状态。 */
export const OPINION_UNIT_FILL_STATUS = {
  PENDING: 'PENDING',
  SUBMITTED: 'SUBMITTED',
} as const

export type OpinionUnitFillStatus = (typeof OPINION_UNIT_FILL_STATUS)[keyof typeof OPINION_UNIT_FILL_STATUS]

export const OPINION_UNIT_FILL_STATUS_TEXT: Record<OpinionUnitFillStatus, string> = {
  PENDING: '待提交',
  SUBMITTED: '已提交',
}

export const OPINION_UNIT_FILL_STATUS_COLOR: Record<OpinionUnitFillStatus, string> = {
  PENDING: 'orange',
  SUBMITTED: 'green',
}

/** 单位填报状态筛选选项。 */
export const OPINION_UNIT_FILL_STATUS_OPTIONS: { value: OpinionUnitFillStatus; label: string }[] = [
  { value: 'PENDING', label: '待提交' },
  { value: 'SUBMITTED', label: '已提交' },
]

/** 基层审核状态。 */
export const OPINION_UNIT_AUDIT_STATUS = {
  NONE: 'NONE',
  PENDING: 'PENDING',
  PASS: 'PASS',
  REJECTED: 'REJECTED',
} as const

export type OpinionUnitAuditStatus = (typeof OPINION_UNIT_AUDIT_STATUS)[keyof typeof OPINION_UNIT_AUDIT_STATUS]

export const OPINION_UNIT_AUDIT_STATUS_TEXT: Record<OpinionUnitAuditStatus, string> = {
  NONE: '未审核',
  PENDING: '待审核',
  PASS: '通过',
  REJECTED: '已退回',
}

export const OPINION_UNIT_AUDIT_STATUS_COLOR: Record<OpinionUnitAuditStatus, string> = {
  NONE: 'gray',
  PENDING: 'orange',
  PASS: 'green',
  REJECTED: 'red',
}

/** 专业部门提交状态。 */
export const OPINION_DEPT_SUBMIT_STATUS = {
  PENDING: 'PENDING',
  SUBMITTED: 'SUBMITTED',
} as const

export type OpinionDeptSubmitStatus = (typeof OPINION_DEPT_SUBMIT_STATUS)[keyof typeof OPINION_DEPT_SUBMIT_STATUS]

export const OPINION_DEPT_SUBMIT_STATUS_TEXT: Record<OpinionDeptSubmitStatus, string> = {
  PENDING: '待提交',
  SUBMITTED: '已提交',
}

export const OPINION_DEPT_SUBMIT_STATUS_COLOR: Record<OpinionDeptSubmitStatus, string> = {
  PENDING: 'orange',
  SUBMITTED: 'green',
}

/** 专业部门审核状态。 */
export const OPINION_DEPT_AUDIT_STATUS = {
  NONE: 'NONE',
  PENDING: 'PENDING',
  PASS: 'PASS',
  REJECTED: 'REJECTED',
} as const

export type OpinionDeptAuditStatus = (typeof OPINION_DEPT_AUDIT_STATUS)[keyof typeof OPINION_DEPT_AUDIT_STATUS]

export const OPINION_DEPT_AUDIT_STATUS_TEXT: Record<OpinionDeptAuditStatus, string> = {
  NONE: '-',
  PENDING: '待审核',
  PASS: '已审核',
  REJECTED: '已退回',
}

export const OPINION_DEPT_AUDIT_STATUS_COLOR: Record<OpinionDeptAuditStatus, string> = {
  NONE: 'gray',
  PENDING: 'orange',
  PASS: 'green',
  REJECTED: 'red',
}
