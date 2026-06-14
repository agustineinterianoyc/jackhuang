package com.hk.demo.app.model.request.opinion.survey;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

/**
 * API-101 / API-102 入参：保存（创建 / 编辑）征集任务草稿。
 */
public class SurveySaveRequest {

    @NotBlank(message = "征集名称不能为空")
    @Size(max = 100, message = "征集名称最长 100 字符")
    private String name;

    @NotNull(message = "考核年份必填")
    @Min(value = 1980, message = "考核年份必须 ≥ 1980")
    @Max(value = 2100, message = "考核年份必须 ≤ 2100")
    private Integer assessYear;

    @Size(max = 1000, message = "通知内容最长 1000 字符")
    private String noticeContent;

    @Size(max = 1000, message = "备注最长 1000 字符")
    private String remark;

    @NotNull(message = "基层截止时间必填")
    private LocalDateTime unitDeadline;

    @NotNull(message = "专业截止时间必填")
    private LocalDateTime deptDeadline;

    @NotEmpty(message = "至少配置一个征集对象")
    @Valid
    private List<TargetItem> targets;

    @NotEmpty(message = "至少配置一个征集模块")
    @Valid
    private List<ModuleItem> modules;

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

    public List<TargetItem> getTargets() {
        return targets;
    }

    public void setTargets(List<TargetItem> targets) {
        this.targets = targets;
    }

    public List<ModuleItem> getModules() {
        return modules;
    }

    public void setModules(List<ModuleItem> modules) {
        this.modules = modules;
    }

    /**
     * 征集对象（单位）。
     */
    public static class TargetItem {

        @NotNull(message = "单位 ID 必填")
        private Long unitId;

        public Long getUnitId() {
            return unitId;
        }

        public void setUnitId(Long unitId) {
            this.unitId = unitId;
        }
    }

    /**
     * 征集模块（指标 Tab）。
     */
    public static class ModuleItem {

        @NotBlank(message = "模块编码必填")
        private String moduleCode;

        @Size(max = 50, message = "模块别称最长 50 字符")
        private String moduleAlias;

        private Integer displayOrder;

        /**
         * 模块附件 fileId 列表（mock 阶段使用伪 fileId）。
         */
        private List<AttachmentItem> attachments;

        public String getModuleCode() {
            return moduleCode;
        }

        public void setModuleCode(String moduleCode) {
            this.moduleCode = moduleCode;
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

        public List<AttachmentItem> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<AttachmentItem> attachments) {
            this.attachments = attachments;
        }
    }

    /**
     * 附件项（mock 阶段：fileId / fileName / fileSize 由前端伪造提交）。
     */
    public static class AttachmentItem {

        @NotBlank(message = "fileId 必填")
        @Size(max = 64, message = "fileId 最长 64 字符")
        private String fileId;

        @NotBlank(message = "fileName 必填")
        @Size(max = 200, message = "fileName 最长 200 字符")
        private String fileName;

        private Long fileSize;

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
