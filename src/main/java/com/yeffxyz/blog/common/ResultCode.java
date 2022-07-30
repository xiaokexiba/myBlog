package com.yeffxyz.blog.common;

/**
 * 错误码
 *
 * @author xoke
 */
public enum ResultCode {
    SUCCESS(2000, "ok", ""),
    PARAMS_ERROR(4000, "请求参数错误", ""),
    NULL_ERROR(4001, "请求数据为空", ""),
    NOT_LOGIN(4010, "未登入", ""),
    NO_AUTH(4002, "无权限", ""),
    SYSTEM_ERROR(5000, "系统内部异常", ""),
    FAIL(201, "失败", ""),
    PARAM_ERROR(202, "参数不正确", ""),
    SERVICE_ERROR(203, "服务异常", ""),
    DATA_ERROR(204, "数据异常", ""),
    DATA_UPDATE_ERROR(205, "数据版本异常", ""),
    CODE_ERROR(210, "验证码错误", "");


    /**
     * 状态码
     */
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述(详情)
     */
    private final String description;

    ResultCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    /**
     * 枚举类没有set方法
     */
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
    }
