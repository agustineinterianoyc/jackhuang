package com.hk.demo.app.model.response.opinion.survey;

import java.time.LocalDateTime;
import java.util.List;

/**
 * API-109 响应：征集任务详情。
 */
public class SurveyDetailVO {

    private Long id;
    private String name;
    private Integer assessYear;
    private String noticeContent;
    private String remark;
    private LocalDateTime unitDeadline;
    private LocalDateTime deptDeadline;
    private String status;
    private String statusText;
    private LocalDateTime startAt;
    private LocalDateTime publishAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<TargetVO> targets;
    private List<ModuleVO> modules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAssessYear() {
        return assessYear;
    }

    public void setAssessYear(Integer assessYear) {
        this.assessYear = assessYear;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getUnitDeadline() {
        return unitDeadline;
    }

    public void setUnitDeadline(LocalDateTime unitDeadline) {
        this.unitDeadline = unitDeadline;
    }

    public LocalDateTime getDeptDeadline() {
        return deptDeadline;
    }

    public void setDeptDeadline(LocalDateTime deptDeadline) {
        this.deptDeadline = deptDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(LocalDateTime publishAt) {
        this.publishAt = publishAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<TargetVO> getTargets() {
        return targets;
    }

    public void setTargets(List<TargetVO> targets) {
        this.targets = targets;
    }

    public List<ModuleVO> getModules() {
        return modules;
    }

    public void setModules(List<ModuleVO> modules) {
        this.modules = modules;
    }

    /**
     * 征集对象明细。
     */
    public static class TargetVO {
        private Long unitId;
        private String unitName;
        private String unitType;
        private String unitTypeText;

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
    }

    /**
     * 征集模块明细。
     */
    public static class ModuleVO {
        private Long id;
        private String moduleCode;
        private String moduleName;
        private String moduleAlias;
        private Integer displayOrder;
        private List<AttachmentVO> attachments;

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

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getModuleAlias() {
            return moduleAlias;
        }

        public void setModuleAlias(String moduleAlias) {
            this.moduleAlias = moduleAlias;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
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
        private Long id;
        private String fileId;
        private String fileName;
        private Long fileSize;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

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

        public Long getFileSize() {
            return fileSize;
        }

        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }
    }
}
