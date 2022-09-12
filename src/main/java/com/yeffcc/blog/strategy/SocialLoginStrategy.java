package com.yeffcc.blog.strategy;

import com.yeffcc.blog.dto.UserInfoDTO;

/**
 * 第三方登入策略
 *
 * @author xoke
 * @date 2022/9/12
 */
public interface SocialLoginStrategy {

    /**
     * 登入
     *
     * @param data 数据
     * @return 用户信息
     */
    UserInfoDTO login(String data);
}
