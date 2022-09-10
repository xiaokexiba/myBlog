package com.yeffcc.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录方式枚举
 *
 * @author xoke
 * @date 2022/9/10
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 邮箱登录
     */
    EMAIL(1, "邮箱登录", ""),
    /**
     * QQ登录
     */
    QQ(2, "QQ登录", "qqLoginStrategyImpl"),
    /**
     * 微博登录
     */
    WECHAT(3, "微信登录", "weChatLoginStrategyImpl");

    /**
     * 登录方式
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 策略
     */
    private final String strategy;


}
