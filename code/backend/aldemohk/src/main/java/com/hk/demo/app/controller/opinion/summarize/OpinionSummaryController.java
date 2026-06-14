package com.hk.demo.app.controller.opinion.summarize;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.model.request.opinion.summary.SummaryListRequest;
import com.hk.demo.app.model.request.opinion.summary.SummarySaveRequest;
import com.hk.demo.app.model.response.opinion.summary.SummaryItemVO;
import com.hk.demo.app.service.opinion.summarize.OpinionSummaryService;
import com.hk.demo.core.response.ApiResponseFactory;
import com.hk.demo.data.pagination.PageResult;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 汇总采纳控制器（绩效管理员 R01）。
 *
 * 实现需求包 STEP-007 关联接口：API-701 / 702 / 703 / 704。
 */
@RestController
@RequestMapping("/api/opinion/summary")
public class OpinionSummaryController {

    private final OpinionSummaryService service;

    public OpinionSummaryController(OpinionSummaryService service) {
        this.service = service;
    }

    /**
     * API-701 汇总采纳清单查询。
     */
    @PostMapping("/{surveyId}/items")
    public ApiResponse<PageResult<SummaryItemVO>> listItems(@PathVariable("surveyId") Long surveyId,
                                                             @Valid @RequestBody SummaryListRequest request) {
        return ApiResponseFactory.success(service.listItems(surveyId, request));
    }

    /**
     * API-702 汇总采纳保存。
     */
    @PostMapping("/{surveyId}/save")
    public ApiResponse<Void> saveItems(@PathVariable("surveyId") Long surveyId,
                                        @Valid @RequestBody SummarySaveRequest request) {
        service.saveItems(surveyId, request);
        return ApiResponseFactory.success(null);
    }

    /**
     * API-703 汇总数据导出。
     */
    @GetMapping("/{surveyId}/export")
    public void exportCsv(@PathVariable("surveyId") Long surveyId,
                          @RequestParam(value = "moduleCode", required = false) String moduleCode,
                          HttpServletResponse response) {
        service.exportCsv(surveyId, moduleCode, response);
    }

    /**
     * API-704 汇总数据导入（首期打桩）。
     */
    @PostMapping("/{surveyId}/import")
    public ApiResponse<Void> importExcel(@PathVariable("surveyId") Long surveyId) {
        service.importExcel(surveyId);
        return ApiResponseFactory.success(null);
    }
}
