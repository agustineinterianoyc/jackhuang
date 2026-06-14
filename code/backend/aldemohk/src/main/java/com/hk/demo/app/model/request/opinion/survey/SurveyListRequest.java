package com.hk.demo.app.model.request.opinion.survey;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * API-108 入参：征集列表查询。
 */
public class SurveyListRequest {

    /** 考核年份（可选）。 */
    private Integer assessYear;

    /** 征集名称模糊（可选）。 */
    @Size(max = 100, message = "征集名称最长 100 字符")
    private String name;

    /**
     * 主状态多选（可选）。
     */
    private List<String> status;

    /** 页码，从 1 开始。 */
    @Min(value = 1, message = "页码不能小于 1")
    private Integer page = 1;

    /** 每页条数，限制 ∈ {10, 20, 50, 100}。 */
    @Min(value = 1, message = "每页条数不能小于 1")
    @Max(value = 200, message = "每页条数不能大于 200")
    private Integer pageSize = 20;

    public Integer getAssessYear() {
        return assessYear;
    }

    public void setAssessYear(Integer assessYear) {
        this.assessYear = assessYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
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
