package com.hk.demo.app.model.response.opinion.deptaudit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 濠电姷鏁搁崑鐐哄垂閸洖绠板┑鐘宠壘缁犳澘顪冪€ｎ亝鎹ｇ紒鐘虫閺岋綁寮崶銉㈠亾閳ь剟鏌涚€ｎ偅宕岀€规洘顨嗗鍕節閸屾瀚熼梻鍌欒兌缁垶銆冮崨顖氼棜妞ゆ挶鍨圭粻顖炴煟濡偐甯涢柡鍛矒閺岋箑螣娓氼垱笑婵犮垻鎳撻悧鎾愁潖濞差亜宸濆┑鐘插閻ｇ數绱撴担鍓叉Ц妞ゆ洦鍙冮幊鐐烘焼瀹ュ懓鎽曢梺闈涱檧闂勫嫬鈻嶉弽顓熲拺?5 闂傚倷娴囧畷鐢稿窗閹扮増鍋￠柕澶堝剻濞戞﹩鐓ラ柛顐墰缁嬪繐鈹戞幊閸婃洟骞婅箛娑樼柧妞ゆ巻鍋撴い顓℃硶閹瑰嫰鎼归悷閭︽殼闂? */
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
     * 濠电姷鏁搁崑娑㈩敋椤撶喐鍙忓Δ锝呭枤閺佸鎲告惔銊ョ疄闁靛ň鏅滈崑鍕攽閸屾凹妲归柛瀣ㄥ劦濮婅櫣绮欓幐搴㈡嫳缂備浇顕х粔瀵稿弲闂佹寧绻傞幆鎾存償閵娿儳鍊為梺鎸庣箓閹冲酣鍩涙径瀣閻庣數顭堢敮鍫曟煙閾忣倖鎴炵┍?     */
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
     * 婵犵數濮烽。钘壩ｉ崨鏉戝瀭妞ゅ繐鐗嗛悞鍨亜閹烘垵鏆為柣婵愪邯閺屾稓鈧絻鍔岄崝姘舵嚕閹扮増鐓曢柕澶樺灣閸掓澘顭胯瑜板啴婀侀梺缁樻尭鐎涒晠宕甸崶顒佺厸濞达絽鎽滄晥閻庤娲滈崢褔鍩為幋锕€鐐婄憸宥嗙珶瀹ュ鈷掗柛灞捐壘閳ь剙鎽滈埀顒佸嚬閸撴盯鍩€椤掍礁鍤ù婊勭矒閸┿垺鎯旈妸銉綂闂侀潧鐗嗗Λ娆忊枍閺嶃劎绡€闁靛骏绲剧涵楣冩煟濡も偓閸熸潙鐣?     */
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
     * 闂傚倸鍊搁崐鎼佸磹閸濄儮鍋撳鐓庡闁逞屽墯閸戣绂嶅鍫濈厺閹兼番鍔岄～鍛存煥濞戞ê顏ら柡鈧幎鑺モ拺闁哄倶鍎插▍鍛存煕閻斿弶娅囨俊鍙夊姍瀵粙顢橀悢鍝勫妇?     */
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
     * 闂傚倸鍊风粈渚€骞夐敓鐘冲仭闁靛／鍛厠閻熸粎澧楃敮鍥焵椤掆偓閹虫﹢銆佸鈧慨鈧柣妯兼暩閺夋悂姊绘繝搴′簻婵炶绠撳畷娆撴偡闁妇鍔烽悗鍏夊亾闁告洦鍓欐禒鈺佲攽閻愭潙鐏︽い顓炴喘閹偟鎹勯妸褏锛滈柡澶婄墑閸斿酣骞婇崶鈹惧亾鐟欏嫭纾搁柛搴ｆ暬閵嗕線寮撮姀鐙€娼婇梺闈涚箳婵敻寮宠箛娑欌拺閻犲洩灏欑粻鎶芥煕鐎ｎ偆娲寸€规洘鍔橀妵鎰板箳閹寸媭妲堕梻浣筋潐椤旀牠宕板Δ鍛獥闁糕剝顦鸿ぐ鎺撴櫜闁搞儱澧庨崝鎼佹⒑閸濆嫬鈧垿宕堕妸褍骞?+ 濠电姷鏁搁崑鐐哄垂閸洖绠板┑鐘宠壘缁犳澘顪冪€ｎ亝鎹ｇ紒鐘虫閺岋綁寮崒姘粯闂佹椿鍘介〃鍫ュ焵椤掆偓缁犲秹宕曢柆宓ュ洭鎸婃竟?+ 闂傚倷娴囬褎顨ラ幖浣稿偍婵犲﹤鐗嗙粈鍫熺節闂堟侗鍎愰柛瀣儔閺屟嗙疀閿濆懍绨介梺宕囨嚀缁夊綊寮诲澶婄厸濞达絽鎲″▓銊х磽娴ｄ粙鍝洪柣妤冨█瀵濡搁妷銏☆潔濠殿喗顨呭Λ娆掑€撮梻?     */
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
        private String remark;

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

        public String getRemark() { return remark; } public void setRemark(String remark) { this.remark = remark; } public String getAdoptionRemark() {
            return adoptionRemark;
        }

        public void setAdoptionRemark(String adoptionRemark) {
            this.adoptionRemark = adoptionRemark;
        }
    }

    /**
     * 闂傚倷娴囬褎顨ラ幖浣稿偍婵犲﹤鐗嗙粈鍫熺節闂堟侗鍎愰柛瀣儔閺岀喖骞嗛悧鍫缂備緡鍋勭粔褰掑蓟閺囩喎绶為柛顐ｇ箓婵垻绱撴担鍝勑繛澶嬬瑜颁線姊洪幖鐐插姷缂佽尪濮ら弲鍫曞箵閹规缍婇幃鈩冩償閵忕姵鐣诲┑?     */
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
