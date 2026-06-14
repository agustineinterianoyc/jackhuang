package com.hk.demo.app.model.dataobject.opinion;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 基层审核日志表（ad_opinion_unit_audit_log）。
 *
 * 日志型表，无逻辑删除字段，无 updated_at。
 */
@TableName("ad_opinion_unit_audit_log")
public class OpinionUnitAuditLogDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("unit_task_id")
    private Long unitTaskId;

    @TableField("survey_id")
    private Long surveyId;

    private String action;

    @TableField("actor_role")
    private String actorRole;

    @TableField("actor_id")
    private Long actorId;

    @TableField("reject_reason")
    private String rejectReason;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnitTaskId() {
        return unitTaskId;
    }

    public void setUnitTaskId(Long unitTaskId) {
        this.unitTaskId = unitTaskId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
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

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
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
