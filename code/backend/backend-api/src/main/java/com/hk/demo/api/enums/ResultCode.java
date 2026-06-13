package com.hk.demo.api.enums;

/**
 * 统一业务返回状态码定义。
 */
public enum ResultCode {

    SUCCESS(200, "success"),
    BAD_REQUEST(400, "请求参数有误"),
    UNAUTHORIZED(401, "未登录或登录已失效"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "请求资源不存在"),
    SYSTEM_ERROR(500, "系统开小差了，请稍后再试"),
    DEMO_DATA_NOT_FOUND(10000, "演示数据不存在"),
    DEMO_BIZ_ERROR(10001, "这是一个演示用业务异常"),
    DICT_TYPE_CODE_DUPLICATE(10100, "字典编码已存在"),
    DICT_TYPE_NOT_FOUND(10101, "字典类型不存在"),
    DICT_ENTRY_VALUE_DUPLICATE(10102, "字典条目值已存在"),
    DICT_ENTRY_NOT_FOUND(10103, "字典条目不存在");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取业务状态码。
     *
     * @return 状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取状态描述信息。
     *
     * @return 描述信息
     */
    public String getMessage() {
        return message;
    }
}
