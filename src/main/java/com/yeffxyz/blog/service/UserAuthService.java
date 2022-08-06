package com.yeffxyz.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.entity.UserAuth;

/**
 * 用户账号业务层接口
 *
 * @author xoke
 * @date 2022/8/6
 */
public interface UserAuthService extends IService<UserAuth> {

    /**
     * 发送验证码
     *
     * @param email 用户邮箱
     */
    void sendCode(String email);

    void userRegister();
}
