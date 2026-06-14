package com.hk.demo.app.model.dataobject.opinion;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hk.demo.data.model.AuditBaseDO;

/**
 * 征集模块明细表（ad_opinion_survey_module）。
 */
@TableName("ad_opinion_survey_module")
public class OpinionSurveyModuleDO extends AuditBaseDO {

    @TableField("survey_id")
    private Long surveyId;

    @TableField("module_code")
    private String moduleCode;

    @TableField("module_name")
    private String moduleName;

    @TableField("module_alias")
    private String moduleAlias;

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

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}
