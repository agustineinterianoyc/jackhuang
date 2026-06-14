package com.hk.demo.app.model.response.opinion.deptaudit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 濠电偞鍨堕幐濠氬箰妞嬪海绠旈柣鏃囥€€閸嬫捇宕楁径濠傗拤闂佺粯顨呯壕顓㈠箯閻樼粯鏅滈柦妯侯槸婢瑰牓姊洪崨濠勭畵缂佸顕懞閬嶅蓟閵夛附娅栭梺?5 闂佽崵鍠愰悷銉ノ涘鍫稏婵°倕鎳忛弲顒€顭跨捄铏瑰闁? */
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
     * 濠电偛顕慨楣冾敋瑜庨幈銊╂偄婵傚鍋ラ梺绋挎湰缁诲秴煤閿濆惓搴ㄥ炊閿濆懎鈷夋繛瀵稿帶閹虫ɑ淇?     */
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
     * 闂備礁鎲￠悷銉х矓瑜版帇鈧懘顢橀姀鐘电杸闂婎偄娲﹂崙鐟邦焽瀹€鍕仩婵炴垶顭囬悞璺ㄧ磼鏉堛劎鎳囩€规洏鍎甸、鏇㈠閵忕姵鏆忛梺璇茬箳閸嬫盯宕愯ぐ鎺戠闁规鍠楅崰鍡椕归敐鍥剁劸闁哄們鍥ㄧ厸?+ 濠电偞鍨堕幐濠氬箰妞嬪海绠旈柣鏃傚帶閻鈧箍鍎遍ˇ钘壝?+ 闂佽楠搁崢婊堝礈濠靛鍋嬮柧蹇ｅ亞閳瑰秹鏌嶉埡浣告殨缂佽鲸鐗犻弻銊モ槈濮楀棙肖闂?     */
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
     * 闂佽楠搁崢婊堝礈濠靛鍋嬮柟鎯版缁秹鏌曟径鍫濆姢缂佺姾娉涜彁闁搞儻绲芥晶鎻捗归悡搴㈠殗濠?     */
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
