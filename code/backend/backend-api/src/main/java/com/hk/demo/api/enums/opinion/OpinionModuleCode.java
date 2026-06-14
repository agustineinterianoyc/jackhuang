package com.hk.demo.api.enums.opinion;

/**
 * 5 类指标 Tab 模块编码。
 */
public enum OpinionModuleCode {

    /** 关键业绩指标。 */
    KPI("关键业绩指标"),

    /** 业绩争取加分。 */
    BONUS("业绩争取加分"),

    /** 党建工作指标。 */
    PARTY("党建工作指标"),

    /** 安全工作指标。 */
    SAFETY("安全工作指标"),

    /** 其他。 */
    OTHER("其他");

    private final String text;

    OpinionModuleCode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * 通过名称查找；找不到时返回 null。
     */
    public static OpinionModuleCode fromName(String name) {
        if (name == null) {
            return null;
        }
        for (OpinionModuleCode m : values()) {
            if (m.name().equals(name)) {
                return m;
            }
        }
        return null;
    }
}
