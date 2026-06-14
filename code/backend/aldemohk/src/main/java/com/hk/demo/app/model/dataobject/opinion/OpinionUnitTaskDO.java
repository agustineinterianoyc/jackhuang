package com.hk.demo.app.model.dataobject.opinion;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hk.demo.data.model.AuditBaseDO;

import java.time.LocalDateTime;

/**
 * 基层任务表（ad_opinion_unit_task）。
 */
@TableName("ad_opinion_unit_task")
public class OpinionUnitTaskDO extends AuditBaseDO {

    @TableField("survey_id")
    private Long surveyId;

    @TableField("unit_id")
    private Long unitId;

    @TableField("unit_name")
    private String unitName;

    @TableField("unit_type")
    private String unitType;

    @TableField("fill_status")
    private String fillStatus;

    @TableField("audit_status")
    private String auditStatus;

    @TableField("auto_zero_report")
    private Integer autoZeroReport;

    @TableField("submitted_at")
    private LocalDateTime submittedAt;

    @TableField("submitted_by")
    private Long submittedBy;

    @TableField("audited_at")
    private LocalDateTime auditedAt;

    @TableField("audited_by")
    private Long auditedBy;

    @TableField("last_reject_reason")
    private String lastRejectReason;

    @TableLogic
    @TableField("deleted_flag")
    private Integer deletedFlag;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
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

    public String getFillStatus() {
        return fillStatus;
    }

    public void setFillStatus(String fillStatus) {
        this.fillStatus = fillStatus;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getAutoZeroReport() {
        return autoZeroReport;
    }

    public void setAutoZeroReport(Integer autoZeroReport) {
        this.autoZeroReport = autoZeroReport;
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

    public LocalDateTime getAuditedAt() {
        return auditedAt;
    }

    public void setAuditedAt(LocalDateTime auditedAt) {
        this.auditedAt = auditedAt;
    }

    public Long getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(Long auditedBy) {
        this.auditedBy = auditedBy;
    }

    public String getLastRejectReason() {
        return lastRejectReason;
    }

    public void setLastRejectReason(String lastRejectReason) {
        this.lastRejectReason = lastRejectReason;
    }

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}
