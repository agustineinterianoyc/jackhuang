package com.hk.demo.app.model.request.opinion.unitfill;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * API-201 入参：基层填报任务列表查询。
 */
public class UnitFillListRequest {

    /** 考核年份（可选）。 */
    private Integer assessYear;

    /** 征集名称模糊（可选）。 */
    @Size(max = 100, message = "征集名称最长 100 字符")
    private String name;

    /**
     * 填报状态多选（可选）。
     */
    private String[] fillStatus;

    /** 页码，从 1 开始。 */
    @Min(value = 1, message = "页码不能小于 1")
    private int page = 1;

    /** 每页条数。 */
    @Min(value = 1, message = "每页条数不能小于 1")
    @Max(value = 200, message = "每页条数不能大于 200")
    private int pageSize = 20;

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

    public String[] getFillStatus() {
        return fillStatus;
    }

    public void setFillStatus(String[] fillStatus) {
        this.fillStatus = fillStatus;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
