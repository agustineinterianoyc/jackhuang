package com.hk.demo.app.controller.opinion.reminder;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.model.request.opinion.reminder.BatchReminderRequest;
import com.hk.demo.app.model.request.opinion.reminder.SingleReminderRequest;
import com.hk.demo.app.model.response.opinion.reminder.BatchReminderVO;
import com.hk.demo.app.service.opinion.reminder.OpinionReminderService;
import com.hk.demo.core.response.ApiResponseFactory;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提醒控制器。
 *
 * 实现需求包 STEP-006 关联接口：API-603 / 604。
 */
@RestController
@RequestMapping("/api/opinion/reminder")
public class OpinionReminderController {

    private final OpinionReminderService service;

    public OpinionReminderController(OpinionReminderService service) {
        this.service = service;
    }

    /**
     * API-603 一键提醒。
     */
    @PostMapping("/batch")
    public ApiResponse<BatchReminderVO> batchRemind(@Valid @RequestBody BatchReminderRequest request) {
        return ApiResponseFactory.success(service.batchRemind(request));
    }

    /**
     * API-604 单条提醒。
     */
    @PostMapping("/single")
    public ApiResponse<Void> singleRemind(@Valid @RequestBody SingleReminderRequest request) {
        service.singleRemind(request);
        return ApiResponseFactory.success(null);
    }
}
