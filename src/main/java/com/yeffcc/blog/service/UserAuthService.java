package com.yeffcc.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffcc.blog.entity.UserAuth;
import com.yeffcc.blog.vo.*;
import com.yeffcc.blog.dto.UserAreaDTO;
import com.yeffcc.blog.dto.UserBackDTO;
import com.yeffcc.blog.dto.UserInfoDTO;
import com.yeffxyz.blog.vo.*;

import java.util.List;

/**
 * 用户账号业务层接口
 *
 * @author xoke
 * @date 2022/8/6
 */
public interface UserAuthService extends IService<UserAuth> {

    /**
     * 发送邮箱验证码
     *
     * @param username 邮箱号
     */
    void sendCode(String username);

    /**
     * 获取用户区域分布
     *
     * @param conditionVO 条件签证官
     * @return {@link List <UserAreaDTO>} 用户区域分布
     */
    List<UserAreaDTO> listUserAreas(ConditionVO conditionVO);

    /**
     * 用户注册
     *
     * @param user 用户对象
     */
    void register(UserVO user);

    /**
     * qq登录
     *
     * @param qqLoginVO qq登录信息
     * @return 用户登录信息
     */
    UserInfoDTO qqLogin(QQLoginVO qqLoginVO);

    /**
     * 微信登录
     *
     * @param weChatLoginVO 微信登录信息
     * @return 用户登录信息
     */
    UserInfoDTO weChatLogin(WeChatLoginVO weChatLoginVO);

    /**
     * 修改密码
     *
     * @param user 用户对象
     */
    void updatePassword(UserVO user);

    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码对象
     */
    void updateAdminPassword(PasswordVO passwordVO);

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    PageResult<UserBackDTO> listUserBackDTO(ConditionVO condition);

}
