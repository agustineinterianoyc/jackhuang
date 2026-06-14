package com.hk.demo.app.model.request.opinion.reminder;

import jakarta.validation.constraints.NotNull;

/**
 * API-604 入参：单条提醒。
 */
public class SingleReminderRequest {

    /** 征集任务 ID（必填）。 */
    @NotNull(message = "征集任务ID不能为空")
    private Long surveyId;

    /** 提醒目标类型（必填，UNIT / DEPT）。 */
    @NotNull(message = "目标类型不能为空")
    private String targetType;

    /** 提醒目标 ID（必填）。 */
    @NotNull(message = "目标ID不能为空")
    private Long targetId;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
