package com.hk.demo.app.service.opinion.summarize;

import com.hk.demo.app.model.request.opinion.summary.SummaryListRequest;
import com.hk.demo.app.model.request.opinion.summary.SummarySaveRequest;
import com.hk.demo.app.model.response.opinion.summary.SummaryItemVO;
import com.hk.demo.data.pagination.PageResult;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 汇总采纳服务。
 *
 * 实现需求包 STEP-007 关联接口：
 * - API-701 汇总采纳清单查询
 * - API-702 汇总采纳保存
 * - API-703 汇总数据导出
 * - API-704 汇总数据导入
 */
public interface OpinionSummaryService {

    /**
     * API-701 汇总采纳清单查询。
     */
    PageResult<SummaryItemVO> listItems(Long surveyId, SummaryListRequest request);

    /**
     * API-702 汇总采纳保存。
     */
    void saveItems(Long surveyId, SummarySaveRequest request);

    /**
     * API-703 汇总数据导出（CSV）。
     */
    void exportCsv(Long surveyId, String moduleCode, HttpServletResponse response);

    /**
     * API-704 汇总数据导入（首期打桩，返回空结果）。
     */
    void importExcel(Long surveyId);
}
