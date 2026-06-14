package com.hk.demo.app.model.response.opinion.unitfill;

import java.time.LocalDateTime;

/**
 * API-201 响应行：基层填报任务列表行。
 */
public class UnitFillListItemVO {

    private Long taskId;
    private Long surveyId;
    private String surveyName;
    private Integer assessYear;
    private Long unitId;
    private String unitName;
    private String fillStatus;
    private String fillStatusText;
    private String auditStatus;
    private String auditStatusText;
    private LocalDateTime unitDeadline;
    private LocalDateTime submittedAt;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public Integer getAssessYear() {
        return assessYear;
    }

    public void setAssessYear(Integer assessYear) {
        this.assessYear = assessYear;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getFillStatus() {
        return fillStatus;
    }

    public void setFillStatus(String fillStatus) {
        this.fillStatus = fillStatus;
    }

    public String getFillStatusText() {
        return fillStatusText;
    }

    public void setFillStatusText(String fillStatusText) {
        this.fillStatusText = fillStatusText;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatusText() {
        return auditStatusText;
    }

    public void setAuditStatusText(String auditStatusText) {
        this.auditStatusText = auditStatusText;
    }

    public LocalDateTime getUnitDeadline() {
        return unitDeadline;
    }

    public void setUnitDeadline(LocalDateTime unitDeadline) {
        this.unitDeadline = unitDeadline;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
