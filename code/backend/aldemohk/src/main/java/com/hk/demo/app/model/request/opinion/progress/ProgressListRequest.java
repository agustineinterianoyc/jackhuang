package com.hk.demo.app.model.request.opinion.progress;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * API-601/602 入参：进度列表查询。
 */
public class ProgressListRequest {

    /** 征集任务 ID（必填）。 */
    @NotNull(message = "征集任务ID不能为空")
    private Long surveyId;

    /** 单位/部门名称模糊搜索（可选）。 */
    private String keyword;

    /** 提交状态多选（可选）。 */
    private List<String> submitStatus;

    /** 页码（必填，≥1）。 */
    @NotNull(message = "页码不能为空")
    private Integer page;

    /** 每页条数（必填）。 */
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<String> getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(List<String> submitStatus) {
        this.submitStatus = submitStatus;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
