package com.hk.demo.app.controller.opinion.deptfeedback;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.model.request.opinion.deptfeedback.DeptFeedbackListRequest;
import com.hk.demo.app.model.request.opinion.deptfeedback.DeptFeedbackSaveRequest;
import com.hk.demo.app.model.response.opinion.deptfeedback.DeptFeedbackDetailVO;
import com.hk.demo.app.model.response.opinion.deptfeedback.DeptFeedbackListItemVO;
import com.hk.demo.app.model.response.opinion.deptfeedback.DeptFeedbackSubmitVO;
import com.hk.demo.app.service.opinion.deptfeedback.OpinionDeptFeedbackService;
import com.hk.demo.core.response.ApiResponseFactory;
import com.hk.demo.data.pagination.PageResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

/**
 * P04 专业部门反馈控制器（专业绩效联络员 R04）。
 *
 * 实现 STEP-005 关联接口：API-401 / 402 / 403 / 404。
 */
@RestController
@RequestMapping("/api/opinion/dept-feedback")
public class OpinionDeptFeedbackController {

    private final OpinionDeptFeedbackService service;

    public OpinionDeptFeedbackController(OpinionDeptFeedbackService service) {
        this.service = service;
    }

    /**
     * API-401 专业反馈任务列表。
     */
    @PostMapping("/list")
    public ApiResponse<PageResult<DeptFeedbackListItemVO>> list(@Valid @RequestBody DeptFeedbackListRequest request) {
        return ApiResponseFactory.success(service.list(request));
    }

    /**
     * API-402 专业反馈任务详情。
     */
    @GetMapping("/{taskId}")
    public ApiResponse<DeptFeedbackDetailVO> detail(@PathVariable("taskId") Long taskId) {
        return ApiResponseFactory.success(service.detail(taskId));
    }

    /**
     * API-403 专业反馈保存草稿。
     */
    @PostMapping("/{taskId}/save")
    public ApiResponse<Void> save(@PathVariable("taskId") Long taskId,
                                  @Valid @RequestBody DeptFeedbackSaveRequest request) {
        service.save(taskId, request);
        return ApiResponseFactory.success(null);
    }

    /**
     * API-404 专业反馈提交。
     */
    @PostMapping("/{taskId}/submit")
    public ApiResponse<DeptFeedbackSubmitVO> submit(@PathVariable("taskId") Long taskId) {
        return ApiResponseFactory.success(service.submit(taskId));
    }

    /**
     * API-802 专业反馈数据导出（CSV）。
     */
    @GetMapping("/{taskId}/export")
    public void export(@PathVariable("taskId") Long taskId, HttpServletResponse response) {
        service.exportCsv(taskId, response);
    }
}
