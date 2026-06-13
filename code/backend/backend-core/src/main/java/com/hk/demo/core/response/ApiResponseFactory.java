package com.hk.demo.core.response;

import com.hk.demo.api.enums.ResultCode;
import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.core.trace.TraceContext;

/**
 * 统一响应对象工厂，负责在返回体中补齐当前请求的 TraceId。
 */
public final class ApiResponseFactory {

    private ApiResponseFactory() {
    }

    /**
     * 构建成功响应，并自动携带当前请求的 TraceId。
     *
     * @param data 返回数据
     * @return 成功响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.success(data, TraceContext.getTraceId());
    }

    /**
     * 构建带自定义提示语的成功响应，并自动携带当前请求的 TraceId。
     *
     * @param message 提示信息
     * @param data 返回数据
     * @return 成功响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.success(message, data, TraceContext.getTraceId());
    }

    /**
     * 构建失败响应，并自动携带当前请求的 TraceId。
     *
     * @param code 业务状态码
     * @param message 失败信息
     * @return 失败响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return ApiResponse.fail(code, message, TraceContext.getTraceId());
    }

    /**
     * 根据统一状态码构建失败响应，并自动携带当前请求的 TraceId。
     *
     * @param resultCode 状态码枚举
     * @return 失败响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> fail(ResultCode resultCode) {
        return ApiResponse.fail(resultCode, TraceContext.getTraceId());
    }
}
