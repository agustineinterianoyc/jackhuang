package com.hk.demo.app.model.dataobject.opinion;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hk.demo.data.model.AuditBaseDO;

import java.time.LocalDateTime;

/**
 * 专业任务表（ad_opinion_dept_task）。
 */
@TableName("ad_opinion_dept_task")
public class OpinionDeptTaskDO extends AuditBaseDO {

    @TableField("survey_id")
    private Long surveyId;

    @TableField("department_id")
    private Long departmentId;

    @TableField("department_name")
    private String departmentName;

    @TableField("module_code")
    private String moduleCode;

    @TableField("submit_status")
    private String submitStatus;

    @TableField("audit_status")
    private String auditStatus;

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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
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
