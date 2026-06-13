package com.hk.demo.core.response;

import com.hk.demo.api.enums.ResultCode;
import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.core.trace.TraceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApiResponseFactoryTests {

    @AfterEach
    void tearDown() {
        TraceContext.clear();
    }

    @Test
    void successShouldCarryCurrentTraceId() {
        TraceContext.setTraceId("trace-success");

        ApiResponse<String> response = ApiResponseFactory.success("payload");

        Assertions.assertEquals(ResultCode.SUCCESS.getCode(), response.getCode());
        Assertions.assertEquals("trace-success", response.getTraceId());
        Assertions.assertEquals("payload", response.getData());
    }

    @Test
    void failShouldCarryCurrentTraceId() {
        TraceContext.setTraceId("trace-fail");

        ApiResponse<Void> response = ApiResponseFactory.fail(ResultCode.SYSTEM_ERROR);

        Assertions.assertEquals(ResultCode.SYSTEM_ERROR.getCode(), response.getCode());
        Assertions.assertEquals("trace-fail", response.getTraceId());
    }
}
