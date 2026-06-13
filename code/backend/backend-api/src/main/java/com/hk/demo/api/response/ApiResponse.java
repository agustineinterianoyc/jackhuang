package com.hk.demo.api.response;

import com.hk.demo.api.enums.ResultCode;

/**
 * 通用接口返回对象。
 *
 * @param <T> 返回数据类型
 */
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private final T data;
    private final String traceId;

    private ApiResponse(int code, String message, T data, String traceId) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.traceId = traceId;
    }

    /**
     * 构建成功响应。
     *
     * @param data 返回数据
     * @return 成功响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, null);
    }

    /**
     * 构建带 TraceId 的成功响应。
     *
     * @param data 返回数据
     * @param traceId 请求链路标识
     * @return 成功响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> success(T data, String traceId) {
        return new ApiResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, traceId);
    }

    /**
     * 构建带自定义提示语的成功响应。
     *
     * @param message 提示信息
     * @param data 返回数据
     * @return 成功响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return success(message, data, null);
    }

    /**
     * 构建带自定义提示语和 TraceId 的成功响应。
     *
     * @param message 提示信息
     * @param data 返回数据
     * @param traceId 请求链路标识
     * @return 成功响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> success(String message, T data, String traceId) {
        return new ApiResponse<>(ResultCode.SUCCESS.getCode(), message, data, traceId);
    }

    /**
     * 构建失败响应。
     *
     * @param code 业务状态码
     * @param message 失败信息
     * @return 失败响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return fail(code, message, null);
    }

    /**
     * 构建带 TraceId 的失败响应。
     *
     * @param code 业务状态码
     * @param message 失败信息
     * @param traceId 请求链路标识
     * @return 失败响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> fail(int code, String message, String traceId) {
        return new ApiResponse<>(code, message, null, traceId);
    }

    /**
     * 根据统一状态码构建失败响应。
     *
     * @param resultCode 状态码枚举
     * @return 失败响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> fail(ResultCode resultCode) {
        return fail(resultCode, null);
    }

    /**
     * 根据统一状态码构建带 TraceId 的失败响应。
     *
     * @param resultCode 状态码枚举
     * @param traceId 请求链路标识
     * @return 失败响应
     * @param <T> 数据类型
     */
    public static <T> ApiResponse<T> fail(ResultCode resultCode, String traceId) {
        return new ApiResponse<>(resultCode.getCode(), resultCode.getMessage(), null, traceId);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    /**
     * 获取本次请求的追踪标识。
     *
     * @return TraceId
     */
    public String getTraceId() {
        return traceId;
    }

}
