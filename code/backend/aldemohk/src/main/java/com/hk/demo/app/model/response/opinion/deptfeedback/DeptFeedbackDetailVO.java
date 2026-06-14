package com.hk.demo.app.model.response.opinion.deptfeedback;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 濠电姷鏁搁崑鐐哄垂閸洖绠板┑鐘宠壘缁犳澘顪冪€ｎ亝鎹ｇ紒鐘虫閺岋綁寮崒姘粯闂佹椿鍘介〃鍫ュ焵椤掆偓缁犲秹宕曢柆宓ュ洭鎸婃竟婵囨そ椤㈡盯鎮欓弶鎴滃寲闂備焦鎮堕崕鎶藉礂濡警鐎舵い鏇楀亾闁哄本绋掗幆鏃堟晬閸曨収鍟嬮柣搴ゎ潐濞诧箓宕戦崟顖ｆ晣濠靛倻顭堝婵囥亜閺嶃劍鐨戦柡鍡樻濮婄粯鎷呴悷閭﹀殝缂備礁顑嗛崹鐢告晝?4 闂傚倷娴囧畷鐢稿窗閹扮増鍋￠柕澶堝剻濞戞﹩鐓ラ柛顐墰缁嬪繐鈹戞幊閸婃洟骞婅箛娑樼柧妞ゆ巻鍋撴い顓℃硶閹瑰嫰鎼归悷閭︽殼闂? */
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
     * 濠电姷鏁搁崑娑㈩敋椤撶喐鍙忓Δ锝呭枤閺佸鎲告惔銊ョ疄闁靛ň鏅滈崑鍕攽閸屾凹妲归柛瀣ㄥ劦濮婅櫣绮欓幐搴㈡嫳缂備浇顕х粔瀵稿弲闂佹寧绻傞幆鎾存償閵娿儳鍊為梺鎸庣箓閹冲酣鍩涙径瀣閻庣數顭堢敮鍫曟煙閾忣倖鎴炵┍?     */
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
     * 闂傚倸鍊风粈渚€骞夐敓鐘冲仭闁靛／鍛厠閻熸粎澧楃敮鍥焵椤掆偓閹虫﹢銆佸鈧慨鈧柣妯兼暩閺夋悂姊绘繝搴′簻婵炶绠撳畷娆撴偡闁妇鍔烽悗鍏夊亾闁告洦鍓欐禒鈺佲攽閻愭潙鐏︽い顓炴喘閹偟鎹勯妸褏锛滈柡澶婄墑閸斿酣骞婇崶鈹惧亾鐟欏嫭纾搁柛搴ｆ暬閵嗕線寮撮姀鐙€娼婇梺闈涚箳婵敻寮宠箛娑欌拺閻犲洩灏欑粻鎶芥煕鐎ｎ偆娲寸€规洘鍔橀妵鎰板箳閹寸媭妲堕梻浣筋潐椤旀牠宕板Δ鍛獥闁糕剝顦鸿ぐ鎺撴櫜闁搞儱澧庨崝鎼佹⒑閸濆嫬鈧垿宕堕妸褍骞?+ 濠电姷鏁搁崑鐐哄垂閸洖绠板┑鐘宠壘缁犳澘顪冪€ｎ亝鎹ｇ紒鐘虫閺岋綁寮崒姘粯闂佹椿鍘介〃鍫ュ焵椤掆偓缁犲秹宕曢柆宓ュ洭鎸婃竟婵囨そ椤㈡盯鎮欑€电寮虫繝鐢靛仦閸ㄥ爼鏁冮埡浣勶綁宕奸弴鐔哄帗?     */
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
}
