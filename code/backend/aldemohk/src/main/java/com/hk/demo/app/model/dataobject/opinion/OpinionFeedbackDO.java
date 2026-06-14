package com.hk.demo.app.model.dataobject.opinion;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hk.demo.data.model.AuditBaseDO;

/**
 * 专业反馈行表（ad_opinion_feedback）。
 */
@TableName("ad_opinion_feedback")
public class OpinionFeedbackDO extends AuditBaseDO {

    @TableField("survey_id")
    private Long surveyId;

    @TableField("dept_task_id")
    private Long deptTaskId;

    @TableField("item_id")
    private Long itemId;

    @TableField("is_adopted")
    private Integer isAdopted;

    @TableField("adoption_remark")
    private String adoptionRemark;

    @TableField("remark")
    private String remark;

    @TableLogic
    @TableField("deleted_flag")
    private Integer deletedFlag;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Long getDeptTaskId() {
        return deptTaskId;
    }

    public void setDeptTaskId(Long deptTaskId) {
        this.deptTaskId = deptTaskId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}
