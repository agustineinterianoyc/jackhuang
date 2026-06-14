package com.hk.demo.api.enums.opinion;

/**
 * 专业部门审核子状态。
 */
public enum OpinionDeptAuditStatus {

    /** 无（未提交时）。 */
    NONE("-"),

    /** 待审核。 */
    PENDING("待审核"),

    /** 已审核。 */
    PASS("已审核"),

    /** 已退回。 */
    REJECTED("已退回");

    private final String text;

    OpinionDeptAuditStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
