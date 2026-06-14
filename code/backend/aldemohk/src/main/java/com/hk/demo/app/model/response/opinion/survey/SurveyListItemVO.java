package com.hk.demo.app.model.response.opinion.survey;

import java.time.LocalDateTime;

/**
 * API-108 响应行：征集列表行。
 */
public class SurveyListItemVO {

    private Long id;
    private String name;
    private Integer assessYear;
    private String status;
    private String statusText;
    private LocalDateTime unitDeadline;
    private LocalDateTime deptDeadline;
    private LocalDateTime createdAt;

    /** 基层填报进度："已提交数 / 总数"。 */
    private String unitProgress;

    /** 专业反馈进度："已提交数 / 总数"，未开启时显示 "-"。 */
    private String deptProgress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUnitProgress() {
        return unitProgress;
    }

    public void setUnitProgress(String unitProgress) {
        this.unitProgress = unitProgress;
    }

    public String getDeptProgress() {
        return deptProgress;
    }

    public void setDeptProgress(String deptProgress) {
        this.deptProgress = deptProgress;
    }
}
