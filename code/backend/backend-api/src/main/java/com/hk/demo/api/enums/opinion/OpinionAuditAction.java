package com.hk.demo.api.enums.opinion;

/**
 * 审核日志动作。
 */
public enum OpinionAuditAction {

    /** 通过。 */
    PASS,

    /** 角色级退回（R03 / R05）。 */
    REJECT,

    /** 绩效管理员退回（R01）。 */
    OPS_REJECT
}
