package com.hk.demo.api.enums.opinion;

/**
 * 基层填报子状态。
 */
public enum OpinionUnitFillStatus {

    /** 待提交。 */
    PENDING("待提交"),

    /** 已提交。 */
    SUBMITTED("已提交");

    private final String text;

    OpinionUnitFillStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
