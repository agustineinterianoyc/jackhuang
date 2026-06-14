package com.hk.demo.api.enums.opinion;

/**
 * 指标体系意见征集模块 — 主状态枚举。
 *
 * 共 6 个状态，与设计基线 v1.0 一致：
 * 草稿 → 待填报 → 填报中 → 专业反馈中 → 已完成 → 已发布
 *
 * 「待发布」不作为独立主状态，而是 DONE 状态下汇总发布页的 UI 子状态。
 */
public enum OpinionMainStatus {

    /** 草稿（节点 10）。 */
    DRAFT("草稿"),

    /** 待填报（节点 20）。 */
    WAIT_FILL("待填报"),

    /** 填报中（节点 30/40）。 */
    FILLING("填报中"),

    /** 专业反馈中（节点 50/60/70）。 */
    DEPT_FEEDBACK("专业反馈中"),

    /** 已完成（节点 80/90）。 */
    DONE("已完成"),

    /** 已发布（节点 100）。 */
    PUBLISHED("已发布");

    private final String text;

    OpinionMainStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * 通过名称查找枚举，找不到时返回 null。
     */
    public static OpinionMainStatus fromName(String name) {
        if (name == null) {
            return null;
        }
        for (OpinionMainStatus s : values()) {
            if (s.name().equals(name)) {
                return s;
            }
        }
        return null;
    }
}
