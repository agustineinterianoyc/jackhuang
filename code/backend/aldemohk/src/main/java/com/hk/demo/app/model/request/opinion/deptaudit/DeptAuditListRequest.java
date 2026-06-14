package com.hk.demo.app.model.request.opinion.deptaudit;

import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * 专业审核列表查询入参（R05 专业部门负责人视角）。
 */
public class DeptAuditListRequest {

    /** 考核年份（可选）。 */
    private Integer assessYear;

    /** 征集名称模糊（可选）。 */
    private String name;

    /** 审核状态多选（可选）。 */
    private List<String> auditStatus;

    /** 页码（从 1 开始）。 */
    @Min(value = 1, message = "页码不能小于 1")
    private int page = 1;

    /** 每页条数。 */
    @Min(value = 1, message = "每页条数不能小于 1")
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

    public List<String> getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(List<String> auditStatus) {
        this.auditStatus = auditStatus;
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
