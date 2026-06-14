package com.hk.demo.api.enums.opinion;

/**
 * 专业部门提交子状态。
 */
public enum OpinionDeptSubmitStatus {

    /** 待提交。 */
    PENDING("待提交"),

    /** 已提交。 */
    SUBMITTED("已提交");

    private final String text;

    OpinionDeptSubmitStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
