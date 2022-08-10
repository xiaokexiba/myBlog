package com.yeffxyz.blog.annotation;

/**
 * redis接口限流
 *
 * @author xoke
 * @date 2022/8/10
 */
public @interface AccessLimit {

    /**
     * 单位时间（秒）
     *
     * @return int
     */
    int seconds();

    /**
     * 单位时间最大请求次数
     *
     * @return int
     */
    int maxCount();
}
