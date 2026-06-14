package com.hk.demo.api.enums.opinion;

/**
 * 意见分类。
 */
public enum OpinionCategory {

    /** 指标定义。 */
    DEFINITION("指标定义"),

    /** 评价标准。 */
    STANDARD("评价标准"),

    /** 指标数据来源。 */
    DATA_SOURCE("指标数据来源"),

    /** 其他。 */
    OTHER("其他");

    private final String text;

    OpinionCategory(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
