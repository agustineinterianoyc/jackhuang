package com.hk.demo.app.model.response.opinion.deptaudit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 专业审核详情响应（R05 视角）。
 */
public class DeptAuditDetailVO {

    private TaskInfo task;
    private List<ModuleInfo> modules;
    private List<FeedbackItemInfo> items;
    private List<AuditLogInfo> auditLogs;

    public TaskInfo getTask() {
        return task;
    }

    public void setTask(TaskInfo task) {
        this.task = task;
    }

    public List<ModuleInfo> getModules() {
        return modules;
    }

    public void setModules(List<ModuleInfo> modules) {
        this.modules = modules;
    }

    public List<FeedbackItemInfo> getItems() {
        return items;
    }

    public void setItems(List<FeedbackItemInfo> items) {
        this.items = items;
    }

    public List<AuditLogInfo> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLogInfo> auditLogs) {
        this.auditLogs = auditLogs;
    }

    /**
     * 任务概要信息。
     */
    public static class TaskInfo {
        private Long id;
        private Long surveyId;
        private String surveyName;
        private Integer assessYear;
        private String surveyStatus;
        private String surveyStatusText;
        private String submitStatus;
        private String submitStatusText;
        private String auditStatus;
        private String auditStatusText;
        private LocalDateTime deptDeadline;
        private LocalDateTime submittedAt;
        private String lastRejectReason;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public String getSurveyStatus() {
            return surveyStatus;
        }

        public void setSurveyStatus(String surveyStatus) {
            this.surveyStatus = surveyStatus;
        }

        public String getSurveyStatusText() {
            return surveyStatusText;
        }

        public void setSurveyStatusText(String surveyStatusText) {
            this.surveyStatusText = surveyStatusText;
        }

        public String getSubmitStatus() {
            return submitStatus;
        }

        public void setSubmitStatus(String submitStatus) {
            this.submitStatus = submitStatus;
        }

        public String getSubmitStatusText() {
            return submitStatusText;
        }

        public void setSubmitStatusText(String submitStatusText) {
            this.submitStatusText = submitStatusText;
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

        public LocalDateTime getDeptDeadline() {
            return deptDeadline;
        }

        public void setDeptDeadline(LocalDateTime deptDeadline) {
            this.deptDeadline = deptDeadline;
        }

        public LocalDateTime getSubmittedAt() {
            return submittedAt;
        }

        public void setSubmittedAt(LocalDateTime submittedAt) {
            this.submittedAt = submittedAt;
        }

        public String getLastRejectReason() {
            return lastRejectReason;
        }

        public void setLastRejectReason(String lastRejectReason) {
            this.lastRejectReason = lastRejectReason;
        }
    }

    /**
     * 模块信息（含附件）。
     */
    public static class ModuleInfo {
        private String moduleCode;
        private String moduleName;
        private List<AttachmentInfo> attachments;

        public String getModuleCode() {
            return moduleCode;
        }

        public void setModuleCode(String moduleCode) {
            this.moduleCode = moduleCode;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public List<AttachmentInfo> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<AttachmentInfo> attachments) {
            this.attachments = attachments;
        }
    }

    /**
     * 附件信息。
     */
    public static class AttachmentInfo {
        private String fileId;
        private String fileName;

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    /**
     * 反馈意见行（含基层意见原文 + 专业反馈 + 审核信息）。
     */
    public static class FeedbackItemInfo {
        private Long itemId;
        private String moduleCode;
        private String indicatorCategory;
        private String indicatorName;
        private String factorName;
        private String unitName;
        private String opinionCategory;
        private String opinionContent;
        private String reason;
        private Integer isAdopted;
        private String adoptionRemark;

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public String getModuleCode() {
            return moduleCode;
        }

        public void setModuleCode(String moduleCode) {
            this.moduleCode = moduleCode;
        }

        public String getIndicatorCategory() {
            return indicatorCategory;
        }

        public void setIndicatorCategory(String indicatorCategory) {
            this.indicatorCategory = indicatorCategory;
        }

        public String getIndicatorName() {
            return indicatorName;
        }

        public void setIndicatorName(String indicatorName) {
            this.indicatorName = indicatorName;
        }

        public String getFactorName() {
            return factorName;
        }

        public void setFactorName(String factorName) {
            this.factorName = factorName;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getOpinionCategory() {
            return opinionCategory;
        }

        public void setOpinionCategory(String opinionCategory) {
            this.opinionCategory = opinionCategory;
        }

        public String getOpinionContent() {
            return opinionContent;
        }

        public void setOpinionContent(String opinionContent) {
            this.opinionContent = opinionContent;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Integer getIsAdopted() {
            return isAdopted;
        }

        public void setIsAdopted(Integer isAdopted) {
            this.isAdopted = isAdopted;
        }

        public String getAdoptionRemark() {
            return adoptionRemark;
        }

        public void setAdoptionRemark(String adoptionRemark) {
            this.adoptionRemark = adoptionRemark;
        }
    }

    /**
     * 审核日志信息。
     */
    public static class AuditLogInfo {
        private Long id;
        private String action;
        private String actorRole;
        private String rejectReason;
        private LocalDateTime createdAt;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getActorRole() {
            return actorRole;
        }

        public void setActorRole(String actorRole) {
            this.actorRole = actorRole;
        }

        public String getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}
