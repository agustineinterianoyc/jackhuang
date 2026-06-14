package com.hk.demo.app.controller.opinion.unitaudit;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.model.request.opinion.unitaudit.UnitAuditListRequest;
import com.hk.demo.app.model.request.opinion.unitaudit.UnitAuditRejectRequest;
import com.hk.demo.app.model.response.opinion.unitaudit.UnitAuditDetailVO;
import com.hk.demo.app.model.response.opinion.unitaudit.UnitAuditListItemVO;
import com.hk.demo.app.model.response.opinion.unitaudit.UnitAuditStatusVO;
import com.hk.demo.app.service.opinion.unitaudit.OpinionUnitAuditService;
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
 * 基层审核管理控制器（R03 人资部主任）。
 *
 * 实现需求包 STEP-004 关联接口：API-301 / 302 / 303 / 304。
 */
@RestController
@RequestMapping("/api/opinion/unit-audit")
public class OpinionUnitAuditController {

    private final OpinionUnitAuditService service;

    public OpinionUnitAuditController(OpinionUnitAuditService service) {
        this.service = service;
    }

    /**
     * API-301 审核列表查询。
     */
    @PostMapping("/list")
    public ApiResponse<PageResult<UnitAuditListItemVO>> list(@Valid @RequestBody UnitAuditListRequest request) {
        return ApiResponseFactory.success(service.list(request));
    }

    /**
     * API-302 审核详情。
     */
    @GetMapping("/{taskId}")
    public ApiResponse<UnitAuditDetailVO> detail(@PathVariable("taskId") Long taskId) {
        return ApiResponseFactory.success(service.detail(taskId));
    }

    /**
     * API-303 审核通过。
     */
    @PostMapping("/{taskId}/pass")
    public ApiResponse<UnitAuditStatusVO> pass(@PathVariable("taskId") Long taskId) {
        return ApiResponseFactory.success(service.pass(taskId));
    }

    /**
     * API-304 审核退回。
     */
    @PostMapping("/{taskId}/reject")
    public ApiResponse<UnitAuditStatusVO> reject(@PathVariable("taskId") Long taskId,
                                                  @Valid @RequestBody UnitAuditRejectRequest request) {
        return ApiResponseFactory.success(service.reject(taskId, request));
    }
}
