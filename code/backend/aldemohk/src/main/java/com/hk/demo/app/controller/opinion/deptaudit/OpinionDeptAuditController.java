package com.hk.demo.app.controller.opinion.deptaudit;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.model.request.opinion.deptaudit.DeptAuditListRequest;
import com.hk.demo.app.model.request.opinion.deptaudit.DeptAuditRejectRequest;
import com.hk.demo.app.model.response.opinion.deptaudit.DeptAuditDetailVO;
import com.hk.demo.app.model.response.opinion.deptaudit.DeptAuditListItemVO;
import com.hk.demo.app.model.response.opinion.deptaudit.DeptAuditStatusVO;
import com.hk.demo.app.service.opinion.deptaudit.OpinionDeptAuditService;
import com.hk.demo.core.response.ApiResponseFactory;
import com.hk.demo.data.pagination.PageResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * P05 专业部门审核控制器（专业部门负责人 R05）。
 *
 * 实现 STEP-005 关联接口：API-501 / 502 / 503 / 504。
 */
@RestController
@RequestMapping("/api/opinion/dept-audit")
public class OpinionDeptAuditController {

    private final OpinionDeptAuditService service;

    public OpinionDeptAuditController(OpinionDeptAuditService service) {
        this.service = service;
    }

    /**
     * API-501 专业审核列表查询。
     */
    @PostMapping("/list")
    public ApiResponse<PageResult<DeptAuditListItemVO>> list(@Valid @RequestBody DeptAuditListRequest request) {
        return ApiResponseFactory.success(service.list(request));
    }

    /**
     * API-502 专业审核详情。
     */
    @GetMapping("/{taskId}")
    public ApiResponse<DeptAuditDetailVO> detail(@PathVariable("taskId") Long taskId) {
        return ApiResponseFactory.success(service.detail(taskId));
    }

    /**
     * API-503 专业审核通过。
     */
    @PostMapping("/{taskId}/pass")
    public ApiResponse<DeptAuditStatusVO> pass(@PathVariable("taskId") Long taskId) {
        return ApiResponseFactory.success(service.pass(taskId));
    }

    /**
     * API-504 专业审核退回。
     */
    @PostMapping("/{taskId}/reject")
    public ApiResponse<DeptAuditStatusVO> reject(@PathVariable("taskId") Long taskId,
                                                  @Valid @RequestBody DeptAuditRejectRequest request) {
        return ApiResponseFactory.success(service.reject(taskId, request));
    }
}
