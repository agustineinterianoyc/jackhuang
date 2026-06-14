package com.hk.demo.app.model.response.opinion.unitfill;

import java.time.LocalDateTime;
import java.util.List;

/**
 * API-202 响应：基层填报任务详情。
 */
public class UnitFillDetailVO {

    private TaskVO task;
    private List<ModuleVO> modules;
    private List<ItemVO> items;
    private List<ItemVO> adjustedItems;

    public TaskVO getTask() {
        return task;
    }

    public void setTask(TaskVO task) {
        this.task = task;
    }

    public List<ModuleVO> getModules() {
        return modules;
    }

    public void setModules(List<ModuleVO> modules) {
        this.modules = modules;
    }

    public List<ItemVO> getItems() {
        return items;
    }

    public void setItems(List<ItemVO> items) {
        this.items = items;
    }

    public List<ItemVO> getAdjustedItems() {
        return adjustedItems;
    }

    public void setAdjustedItems(List<ItemVO> adjustedItems) {
        this.adjustedItems = adjustedItems;
    }

    /**
     * 任务基本信息。
     */
    public static class TaskVO {
        private Long id;
        private Long surveyId;
        private String surveyName;
        private Integer assessYear;
        private String surveyStatus;
        private String surveyStatusText;
        private String fillStatus;
        private String fillStatusText;
        private String auditStatus;
        private String auditStatusText;
        private LocalDateTime unitDeadline;

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
    }

    /**
     * 征集模块。
     */
    public static class ModuleVO {
        private String moduleCode;
        private String moduleName;
        private List<AttachmentVO> attachments;

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

        public List<AttachmentVO> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<AttachmentVO> attachments) {
            this.attachments = attachments;
        }
    }

    /**
     * 附件元数据。
     */
    public static class AttachmentVO {
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
     * 意见条目。
     */
    public static class ItemVO {
        private Long id;
        private String moduleCode;
        private String indicatorCategory;
        private String indicatorName;
        private String factorName;
        private String extraField;
        private String opinionCategory;
        private String opinionContent;
        private String reason;
        private Integer displayOrder;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public String getExtraField() {
            return extraField;
        }

        public void setExtraField(String extraField) {
            this.extraField = extraField;
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

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }
    }
}
