package com.hk.demo.app.controller.opinion.unitfill;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.model.request.opinion.unitfill.UnitFillListRequest;
import com.hk.demo.app.model.request.opinion.unitfill.UnitFillSaveRequest;
import com.hk.demo.app.model.response.opinion.unitfill.UnitFillDetailVO;
import com.hk.demo.app.model.response.opinion.unitfill.UnitFillListItemVO;
import com.hk.demo.app.model.response.opinion.unitfill.UnitFillSubmitVO;
import com.hk.demo.app.service.opinion.unitfill.OpinionUnitFillService;
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
 * P02 基层填报控制器（基层绩效管理员 R02）。
 *
 * 实现需求包 STEP-004 关联接口：API-201 / 202 / 203 / 204。
 */
@RestController
@RequestMapping("/api/opinion/unit-fill")
public class OpinionUnitFillController {

    private final OpinionUnitFillService service;

    public OpinionUnitFillController(OpinionUnitFillService service) {
        this.service = service;
    }

    /**
     * API-201 基层填报任务列表。
     */
    @PostMapping("/list")
    public ApiResponse<PageResult<UnitFillListItemVO>> list(@Valid @RequestBody UnitFillListRequest request) {
        return ApiResponseFactory.success(service.list(request));
    }

    /**
     * API-202 基层填报任务详情。
     */
    @GetMapping("/{taskId}")
    public ApiResponse<UnitFillDetailVO> detail(@PathVariable("taskId") Long taskId) {
        return ApiResponseFactory.success(service.detail(taskId));
    }

    /**
     * API-203 基层填报意见保存。
     */
    @PostMapping("/{taskId}/save")
    public ApiResponse<Void> save(@PathVariable("taskId") Long taskId,
                                  @Valid @RequestBody UnitFillSaveRequest request) {
        service.save(taskId, request);
        return ApiResponseFactory.success(null);
    }

    /**
     * API-204 基层填报提交。
     */
    @PostMapping("/{taskId}/submit")
    public ApiResponse<UnitFillSubmitVO> submit(@PathVariable("taskId") Long taskId) {
        return ApiResponseFactory.success(service.submit(taskId));
    }
}
