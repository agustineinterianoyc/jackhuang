package com.hk.demo.app.model.dataobject.opinion;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hk.demo.data.model.AuditBaseDO;

import java.time.LocalDateTime;

/**
 * 征集任务主表（ad_opinion_survey）。
 */
@TableName("ad_opinion_survey")
public class OpinionSurveyDO extends AuditBaseDO {

    private String name;

    @TableField("assess_year")
    private Integer assessYear;

    @TableField("notice_content")
    private String noticeContent;

    private String remark;

    @TableField("unit_deadline")
    private LocalDateTime unitDeadline;

    @TableField("dept_deadline")
    private LocalDateTime deptDeadline;

    private String status;

    @TableField("start_at")
    private LocalDateTime startAt;

    @TableField("dept_open_at")
    private LocalDateTime deptOpenAt;

    @TableField("publish_at")
    private LocalDateTime publishAt;

    @TableField("created_by")
    private Long createdBy;

    @TableField("updated_by")
    private Long updatedBy;

    @TableLogic
    @TableField("deleted_flag")
    private Integer deletedFlag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAssessYear() {
        return assessYear;
    }

    public void setAssessYear(Integer assessYear) {
        this.assessYear = assessYear;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getUnitDeadline() {
        return unitDeadline;
    }

    public void setUnitDeadline(LocalDateTime unitDeadline) {
        this.unitDeadline = unitDeadline;
    }

    public LocalDateTime getDeptDeadline() {
        return deptDeadline;
    }

    public void setDeptDeadline(LocalDateTime deptDeadline) {
        this.deptDeadline = deptDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getDeptOpenAt() {
        return deptOpenAt;
    }

    public void setDeptOpenAt(LocalDateTime deptOpenAt) {
        this.deptOpenAt = deptOpenAt;
    }

    public LocalDateTime getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(LocalDateTime publishAt) {
        this.publishAt = publishAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}
