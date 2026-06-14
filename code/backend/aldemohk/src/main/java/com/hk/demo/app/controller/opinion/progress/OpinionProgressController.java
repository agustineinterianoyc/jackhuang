package com.hk.demo.app.controller.opinion.progress;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.model.request.opinion.progress.ProgressListRequest;
import com.hk.demo.app.model.response.opinion.progress.DeptProgressVO;
import com.hk.demo.app.model.response.opinion.progress.UnitProgressVO;
import com.hk.demo.app.service.opinion.progress.OpinionProgressService;
import com.hk.demo.core.response.ApiResponseFactory;
import com.hk.demo.data.pagination.PageResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 进度查询控制器。
 *
 * 实现需求包 STEP-006 关联接口：API-601 / 602。
 */
@RestController
@RequestMapping("/api/opinion/progress")
public class OpinionProgressController {

    private final OpinionProgressService service;

    public OpinionProgressController(OpinionProgressService service) {
        this.service = service;
    }

    /**
     * API-601 基层进度查询。
     */
    @PostMapping("/units")
    public ApiResponse<PageResult<UnitProgressVO>> listUnits(@Valid @RequestBody ProgressListRequest request) {
        return ApiResponseFactory.success(service.listUnits(request));
    }

    /**
     * API-602 专业进度查询。
     */
    @PostMapping("/depts")
    public ApiResponse<PageResult<DeptProgressVO>> listDepts(@Valid @RequestBody ProgressListRequest request) {
        return ApiResponseFactory.success(service.listDepts(request));
    }
}
