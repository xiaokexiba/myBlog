package com.yeffxyz.blog.exception;

import com.yeffxyz.blog.enums.StatusCodeEnum;

import static com.yeffxyz.blog.enums.StatusCodeEnum.FAIL;

/**
 * 自定义异常类
 *
 * @author xoke
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = FAIL.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public BusinessException(String message) {
        this.message = message;
    }

    public BusinessException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getDesc();
    }
}
