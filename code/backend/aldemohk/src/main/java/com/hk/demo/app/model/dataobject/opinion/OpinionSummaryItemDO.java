package com.hk.demo.app.model.dataobject.opinion;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hk.demo.data.model.AuditBaseDO;

/**
 * 汇总采纳行表（ad_opinion_summary_item）。
 */
@TableName("ad_opinion_summary_item")
public class OpinionSummaryItemDO extends AuditBaseDO {

    @TableField("survey_id")
    private Long surveyId;

    @TableField("feedback_id")
    private Long feedbackId;

    @TableField("item_id")
    private Long itemId;

    @TableField("final_is_adopted")
    private Integer finalIsAdopted;

    @TableField("final_adoption_remark")
    private String finalAdoptionRemark;

    @TableField("adjusted_content")
    private String adjustedContent;

    @TableLogic
    @TableField("deleted_flag")
    private Integer deletedFlag;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
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

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}
