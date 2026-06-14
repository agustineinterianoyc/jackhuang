package com.hk.demo.app.model.dataobject.opinion;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 征集对象表（ad_opinion_survey_target）。
 *
 * 该表无 updated_at 字段，故不继承 AuditBaseDO。
 */
@TableName("ad_opinion_survey_target")
public class OpinionSurveyTargetDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("survey_id")
    private Long surveyId;

    @TableField("unit_id")
    private Long unitId;

    @TableField("unit_type")
    private String unitType;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableLogic
    @TableField("deleted_flag")
    private Integer deletedFlag;

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

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}
