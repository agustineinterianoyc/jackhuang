package com.hk.demo.app.model.request.opinion.summary;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * API-701 入参：汇总采纳清单查询。
 */
public class SummaryListRequest {

    /** 模块编码（可选过滤）。 */
    @Size(max = 50, message = "模块编码最长 50 字符")
    private String moduleCode;

    /** 页码，从 1 开始。 */
    @Min(value = 1, message = "页码不能小于 1")
    private Integer page = 1;

    /** 每页条数。 */
    @Min(value = 1, message = "每页条数不能小于 1")
    private Integer pageSize = 20;

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
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
