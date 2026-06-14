package com.hk.demo.app.model.response.opinion.deptfeedback;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 濠电偞鍨堕幐濠氬箰妞嬪海绠旈柣鏃傚帶閻鈧箍鍎遍ˇ钘壝洪鐐村仯闁搞儳鍏樺顕€鏌涙惔锛勭鐎规洦鍋勯濂稿川椤栨氨鏆旈梻浣瑰缁嬫垵锕?4 闂佽崵鍠愰悷銉ノ涘鍫稏婵°倕鎳忛弲顒€顭跨捄铏瑰闁? */
public class DeptFeedbackDetailVO {

    private TaskInfo task;
    private List<ModuleInfo> modules;
    private List<FeedbackItemInfo> items;

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

    /**
     * 濠电偛顕慨楣冾敋瑜庨幈銊╂偄婵傚鍋ラ梺绋挎湰缁诲秴煤閿濆惓搴ㄥ炊閿濆懎鈷夋繛瀵稿帶閹虫ɑ淇?     */
    public static class TaskInfo {
        private Long id;
        private Long surveyId;
        private String surveyName;
        private Integer assessYear;
        private String surveyStatus;
        private String submitStatus;
        private String auditStatus;
        private LocalDateTime deptDeadline;
        private LocalDateTime submittedAt;

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
    }

    /**
     * 婵犵妲呴崹顏堝焵椤掑啯鐝柛瀣ㄥ劚鑿愰柛銉到婢ф彃霉閻撳孩鍤囬柡浣哥Ч瀹曠厧鈹戦崶褍澹嶉梻鍌氬€哥€氼剛鈧凹浜滈埢搴ㄥ閵堝棙娅栨繝銏ｆ硾閻楀啴宕?     */
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
     * 闂傚倸鍊哥€氼剛鈧凹浜滈埢搴ㄥ閿涘嫧鏀抽梺鏂ユ櫅閸熲晝妲愰弽顓熺厪?     */
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
     * 闂備礁鎲￠悷銉х矓瑜版帇鈧懘顢橀姀鐘电杸闂婎偄娲﹂崙鐟邦焽瀹€鍕仩婵炴垶顭囬悞璺ㄧ磼鏉堛劎鎳囩€规洏鍎甸、鏇㈠閵忕姵鏆忛梺璇茬箳閸嬫盯宕愯ぐ鎺戠闁规鍠楅崰鍡椕归敐鍥剁劸闁哄們鍥ㄧ厸?+ 濠电偞鍨堕幐濠氬箰妞嬪海绠旈柣鏃傚帶閻鈧箍鍎遍ˇ钘壝洪鐐寸叆婵炴垶锕╁Σ鍫曟煃?     */
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
}
