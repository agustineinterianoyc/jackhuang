package com.hk.demo.app.model.response.opinion.summary;

/**
 * API-701 响应项：汇总采纳清单行。
 */
public class SummaryItemVO {

    private Long id;
    private Long feedbackId;
    private Long itemId;
    private String moduleCode;
    private String indicatorCategory;
    private String indicatorName;
    private String factorName;
    private String unitName;
    private String opinionCategory;
    private String opinionContent;
    private String reason;
    private String deptName;
    private Integer isAdopted;
    private String adoptionRemark;
    private Integer finalIsAdopted;
    private String finalAdoptionRemark;
    private String adjustedContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public Integer getFinalIsAdopted() {
        return finalIsAdopted;
    }

    public void setFinalIsAdopted(Integer finalIsAdopted) {
        this.finalIsAdopted = finalIsAdopted;
    }

    public String getFinalAdoptionRemark() {
        return finalAdoptionRemark;
    }

    public void setFinalAdoptionRemark(String finalAdoptionRemark) {
        this.finalAdoptionRemark = finalAdoptionRemark;
    }

    public String getAdjustedContent() {
        return adjustedContent;
    }

    public void setAdjustedContent(String adjustedContent) {
        this.adjustedContent = adjustedContent;
    }
}
