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
    DICT_ENTRY_NOT_FOUND(10103, "字典条目不存在"),

    // ===== 指标体系意见征集模块（opinion） 11000–11099 =====
    OPINION_NOT_FOUND(11000, "意见征集任务不存在"),
    OPINION_DUPLICATE_NAME(11001, "同年同名的意见征集任务已存在"),
    OPINION_ILLEGAL_STATE(11002, "当前状态不允许执行该操作"),
    OPINION_INCOMPLETE_CONFIG(11003, "征集对象或征集模块未配置完整"),
    OPINION_DEADLINE_INVALID(11004, "基层 / 专业截止时间不合法"),
    OPINION_EMPTY_NOT_ALLOWED(11005, "意见列表不能为空（仅零报送时系统自动提交）"),
    OPINION_ADOPTION_REMARK_REQUIRED(11006, "采纳=是时必须填写采纳说明"),
    OPINION_REJECT_REASON_REQUIRED(11007, "退回时必须填写退回原因"),
    OPINION_PUBLISHED_READONLY(11008, "已发布的征集任务不可修改"),
    OPINION_NOT_DRAFT(11009, "仅草稿状态可执行该操作"),
    OPINION_TASK_NOT_FOUND(11010, "基层 / 专业任务不存在");

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
