package com.hk.demo.api.enums.opinion;

/**
 * 单位类型。
 */
public enum OpinionUnitType {

    /** 供电单位。 */
    POWER("供电单位"),

    /** 业务支撑单位。 */
    SUPPORT("业务支撑单位"),

    /** 市场化单位。 */
    MARKET("市场化单位"),

    /** 其他单位。 */
    OTHER("其他单位");

    private final String text;

    OpinionUnitType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
