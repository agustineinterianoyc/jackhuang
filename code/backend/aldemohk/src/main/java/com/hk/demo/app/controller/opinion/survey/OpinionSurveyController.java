package com.hk.demo.app.controller.opinion.survey;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.model.request.opinion.survey.SurveyListRequest;
import com.hk.demo.app.model.request.opinion.survey.SurveySaveRequest;
import com.hk.demo.app.model.response.opinion.survey.SurveyDetailVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyListItemVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyPublishVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyStartVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyStatusVO;
import com.hk.demo.app.service.opinion.survey.OpinionSurveyService;
import com.hk.demo.core.response.ApiResponseFactory;
import com.hk.demo.data.pagination.PageResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * P01 意见征集管理控制器（绩效管理员 R01）。
 *
 * 实现需求包 STEP-003 关联接口：API-101 / 102 / 103 / 104 / 105 / 108 / 109 / 110 / 111。
 *
 * TODO(STEP-007): API-106 / API-107 / 汇总采纳与发布。
 */
@RestController
@RequestMapping("/api/opinion/survey")
public class OpinionSurveyController {

    private final OpinionSurveyService service;

    public OpinionSurveyController(OpinionSurveyService service) {
        this.service = service;
    }

    /**
     * API-101 创建征集（草稿）。
     */
    @PostMapping
    public ApiResponse<SurveyStatusVO> create(@Valid @RequestBody SurveySaveRequest request) {
        return ApiResponseFactory.success(service.create(request));
    }

    /**
     * API-102 编辑征集（草稿）。
     */
    @PutMapping("/{id}")
    public ApiResponse<SurveyStatusVO> update(@PathVariable("id") Long id,
                                              @Valid @RequestBody SurveySaveRequest request) {
        return ApiResponseFactory.success(service.update(id, request));
    }

    /**
     * API-103 删除征集（草稿）。
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ApiResponseFactory.success(null);
    }

    /**
     * API-104 开启征集。
     */
    @PostMapping("/{id}/start")
    public ApiResponse<SurveyStartVO> start(@PathVariable("id") Long id) {
        return ApiResponseFactory.success(service.start(id));
    }

    /**
     * API-105 开启专业反馈。
     */
    @PostMapping("/{id}/open-dept-feedback")
    public ApiResponse<SurveyStatusVO> openDeptFeedback(@PathVariable("id") Long id) {
        return ApiResponseFactory.success(service.openDeptFeedback(id));
    }

    /**
     * API-108 征集列表查询。
     */
    @PostMapping("/list")
    public ApiResponse<PageResult<SurveyListItemVO>> list(@Valid @RequestBody SurveyListRequest request) {
        return ApiResponseFactory.success(service.list(request));
    }

    /**
     * API-109 征集任务详情。
     */
    @GetMapping("/{id}")
    public ApiResponse<SurveyDetailVO> detail(@PathVariable("id") Long id) {
        return ApiResponseFactory.success(service.detail(id));
    }

    /**
     * API-110 绩效退回基层。
     */
    @PostMapping("/{id}/ops-reject-unit")
    public ApiResponse<SurveyStatusVO> opsRejectUnit(@PathVariable("id") Long id,
                                                      @RequestBody Map<String, String> body) {
        String rejectReason = body.getOrDefault("rejectReason", "");
        return ApiResponseFactory.success(service.opsRejectUnit(id, body.get("unitTaskId") != null ?
            Long.parseLong(body.get("unitTaskId")) : null, rejectReason));
    }

    /**
     * API-111 绩效退回专业。
     */
    @PostMapping("/{id}/ops-reject-dept")
    public ApiResponse<SurveyStatusVO> opsRejectDept(@PathVariable("id") Long id,
                                                      @RequestBody Map<String, String> body) {
        String rejectReason = body.getOrDefault("rejectReason", "");
        return ApiResponseFactory.success(service.opsRejectDept(id, body.get("deptTaskId") != null ?
            Long.parseLong(body.get("deptTaskId")) : null, rejectReason));
    }

    /**
     * API-106 汇总（推进 DONE + 初始化汇总采纳行）。
     */
    @PostMapping("/{id}/summarize")
    public ApiResponse<SurveyStatusVO> summarize(@PathVariable("id") Long id) {
        return ApiResponseFactory.success(service.summarize(id));
    }

    /**
     * API-107 发布（DONE → PUBLISHED）。
     */
    @PostMapping("/{id}/publish")
    public ApiResponse<SurveyPublishVO> publish(@PathVariable("id") Long id) {
        return ApiResponseFactory.success(service.publish(id));
    }
}
