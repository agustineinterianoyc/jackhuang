package com.hk.demo.app.model.dataobject.opinion;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hk.demo.data.model.AuditBaseDO;

/**
 * 基层意见行表（ad_opinion_item）。
 */
@TableName("ad_opinion_item")
public class OpinionItemDO extends AuditBaseDO {

    @TableField("survey_id")
    private Long surveyId;

    @TableField("unit_task_id")
    private Long unitTaskId;

    @TableField("unit_id")
    private Long unitId;

    @TableField("module_code")
    private String moduleCode;

    @TableField("indicator_category")
    private String indicatorCategory;

    @TableField("indicator_name")
    private String indicatorName;

    @TableField("factor_name")
    private String factorName;

    @TableField("extra_field")
    private String extraField;

    @TableField("opinion_category")
    private String opinionCategory;

    @TableField("opinion_content")
    private String opinionContent;

    private String reason;

    @TableField("display_order")
    private Integer displayOrder;

    @TableLogic
    @TableField("deleted_flag")
    private Integer deletedFlag;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Long getUnitTaskId() {
        return unitTaskId;
    }

    public void setUnitTaskId(Long unitTaskId) {
        this.unitTaskId = unitTaskId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
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

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}
