package com.hk.demo.app.model.request.opinion.unitaudit;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * API-301 入参：基层审核列表查询（R03 人资部主任视角）。
 */
public class UnitAuditListRequest {

    /** 考核年份（可选）。 */
    private Integer assessYear;

    /** 征集任务名称模糊（可选）。 */
    private String name;

    /** 审核状态多选（可选）。 */
    private List<String> auditStatus;

    /** 页码（必填，≥1）。 */
    @NotNull(message = "页码不能为空")
    private Integer page;

    /** 每页条数（必填）。 */
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize;

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

    public List<String> getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(List<String> auditStatus) {
        this.auditStatus = auditStatus;
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
