package com.yeffxyz.blog.common;

/**
 * 返回工具类
 *
 * @author xoke
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param resultCode
     * @return
     */
    public static BaseResponse error(ResultCode resultCode) {
        return new BaseResponse<>(resultCode);
    }

    /**
     * 失败
     *
     * @param resultCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ResultCode resultCode, String message, String description) {
        return new BaseResponse<>(resultCode.getCode(), null, message, description);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     * 失败
     *
     * @param resultCode
     * @param description
     * @return
     */
    public static BaseResponse error(ResultCode resultCode, String description) {
        return new BaseResponse<>(resultCode.getCode(), resultCode.getMessage(), description);
    }

}
