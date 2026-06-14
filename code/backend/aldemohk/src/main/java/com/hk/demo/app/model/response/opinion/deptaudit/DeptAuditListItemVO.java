package com.hk.demo.app.model.response.opinion.deptaudit;

import java.time.LocalDateTime;

/**
 * 专业审核列表行（R05 视角）。
 */
public class DeptAuditListItemVO {

    private Long id;
    private Long surveyId;
    private String surveyName;
    private Integer assessYear;
    private String moduleCode;
    private String moduleName;
    private LocalDateTime deptDeadline;
    private String submitStatus;
    private String auditStatus;
    private String surveyStatus;

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

    public LocalDateTime getDeptDeadline() {
        return deptDeadline;
    }

    public void setDeptDeadline(LocalDateTime deptDeadline) {
        this.deptDeadline = deptDeadline;
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

    public String getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(String surveyStatus) {
        this.surveyStatus = surveyStatus;
    }
}
