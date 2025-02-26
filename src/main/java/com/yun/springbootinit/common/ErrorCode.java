package com.yun.springbootinit.common;

/**
 * 自定义错误码
 *
 * @author chenyun
 * 
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(400, "请求参数错误"),
    NOT_LOGIN_ERROR(401, "未登录"),
    NO_AUTH_OR_FORBIDDEN_ERROR(403, "无权限/禁止访问"),
    NOT_FOUND_ERROR(404, "请求数据不存在"),
    TOO_MANY_REQUEST(429, "请求过于频繁"),
    SYSTEM_ERROR(500, "系统内部异常"),
    OPERATION_ERROR(501, "操作失败"),
    FILE_OPERATE_ERROR(502, "文件操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
