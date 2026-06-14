package com.hk.demo.app.model.response.opinion.progress;

import java.time.LocalDateTime;

/**
 * API-601 响应行：基层进度列表行。
 */
public class UnitProgressVO {

    private Long taskId;
    private Long unitId;
    private String unitName;
    private String unitType;
    private String unitTypeText;
    private String fillStatus;
    private String fillStatusText;
    private String auditStatus;
    private String auditStatusText;
    private LocalDateTime submittedAt;
    private Long submittedBy;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitTypeText() {
        return unitTypeText;
    }

    public void setUnitTypeText(String unitTypeText) {
        this.unitTypeText = unitTypeText;
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

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Long getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(Long submittedBy) {
        this.submittedBy = submittedBy;
    }
}
