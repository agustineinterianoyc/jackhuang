package com.hk.demo.app.model.request.opinion.reminder;

import jakarta.validation.constraints.NotNull;

/**
 * API-603 入参：一键提醒。
 */
public class BatchReminderRequest {

    /** 征集任务 ID（必填）。 */
    @NotNull(message = "征集任务ID不能为空")
    private Long surveyId;

    /** 提醒目标类型（必填，UNIT / DEPT）。 */
    @NotNull(message = "目标类型不能为空")
    private String targetType;

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
}
