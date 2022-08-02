package com.yeffxyz.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户业务层接口
 *
 * @author xoke
 * @date 2022/7/27
 */
public interface UserService extends IService<User> {

    /**
     * 用户注释
     *
     * @param username      用户账户
     * @param password      用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String username, String password, String checkPassword);

    /**
     * 用户登入
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      前端数据
     * @return 脱敏后的用户数据
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser 原始用户
     * @return 脱敏后用户
     */
    User getSafetyUser(User originUser);

    /**
     * 用户退出
     *
     * @param request 前端数据
     * @return 退出成功
     */
    int userLogout(HttpServletRequest request);
}
