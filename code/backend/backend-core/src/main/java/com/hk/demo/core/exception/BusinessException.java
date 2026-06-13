package com.hk.demo.core.exception;

import com.hk.demo.api.enums.ResultCode;

/**
 * 业务异常定义，用于承载统一业务状态码和提示信息。
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 使用统一状态码枚举创建业务异常。
     *
     * @param resultCode 业务状态码枚举
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 获取业务异常编码。
     *
     * @return 业务编码
     */
    public int getCode() {
        return code;
    }
}
