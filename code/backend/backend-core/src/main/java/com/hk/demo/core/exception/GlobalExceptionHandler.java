package com.hk.demo.core.exception;

import com.hk.demo.api.enums.ResultCode;
import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.core.response.ApiResponseFactory;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，统一收口接口错误返回和异常日志。
 */
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常。
     *
     * @param exception 业务异常
     * @return 统一错误响应
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException exception) {
        log.warn("Business exception happened: code={}, message={}", exception.getCode(), exception.getMessage());
        return ApiResponseFactory.fail(exception.getCode(), exception.getMessage());
    }

    /**
     * 处理请求体参数校验异常。
     *
     * @param exception 参数校验异常
     * @return 统一错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getField() + " " + error.getDefaultMessage())
            .orElse(ResultCode.BAD_REQUEST.getMessage());
        log.warn("Method argument not valid: {}", message);
        return ApiResponseFactory.fail(ResultCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理简单参数校验异常。
     *
     * @param exception 参数校验异常
     * @return 统一错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException exception) {
        log.warn("Constraint violation: {}", exception.getMessage());
        return ApiResponseFactory.fail(ResultCode.BAD_REQUEST.getCode(), exception.getMessage());
    }

    /**
     * 兜底处理未预期异常。
     *
     * @param exception 系统异常
     * @return 统一错误响应
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception exception) {
        log.error("Unhandled exception happened", exception);
        return ApiResponseFactory.fail(ResultCode.SYSTEM_ERROR);
    }
}
