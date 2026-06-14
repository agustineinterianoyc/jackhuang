package com.hk.demo.api.enums.opinion;

/**
 * 基层审核子状态。
 *
 * NONE 表示该任务尚未提交、不存在审核动作。
 */
public enum OpinionUnitAuditStatus {

    /** 无（未提交时）。 */
    NONE("-"),

    /** 待审核。 */
    PENDING("待审核"),

    /** 已审核 / 已通过。 */
    PASS("已通过"),

    /** 已退回。 */
    REJECTED("已退回");

    private final String text;

    OpinionUnitAuditStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
