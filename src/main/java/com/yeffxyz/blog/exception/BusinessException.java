package com.yeffxyz.blog.exception;

import com.yeffxyz.blog.enums.StatusCodeEnum;

/**
 * 自定义异常类
 *
 * @author xoke
 */
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(StatusCodeEnum statusCodeEnum) {
        super(statusCodeEnum.getMessage());
        this.code = statusCodeEnum.getCode();
        this.description = statusCodeEnum.getDescription();
    }

    public BusinessException(StatusCodeEnum statusCodeEnum, String description) {
        super(statusCodeEnum.getMessage());
        this.code = statusCodeEnum.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
