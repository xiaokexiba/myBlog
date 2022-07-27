package com.yeffxyz.blog.service;

import com.yeffxyz.blog.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户业务层接口
 *
 * @author xoke
 * @date 2022/7/27
 */
public interface UserService {
    /**
     * 核对管理员用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 脱敏后的用户数据
     */
    User checkUser(String username, String password);

    /**
     * 用户脱敏
     *
     * @param originUser 原始数据
     * @return 脱敏后的用户数据
     */
    User getSafetyUser(User originUser);
}
