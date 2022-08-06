package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.common.ResultCode;
import com.yeffxyz.blog.entity.UserInfo;
import com.yeffxyz.blog.exception.BusinessException;
import com.yeffxyz.blog.mapper.UserMapper;
import com.yeffxyz.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yeffxyz.blog.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户业务层接口实现类
 *
 * @author xoke
 * @date 2022/7/27
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserInfo> implements UserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 用户名长度
     */
    public static int usernameLength = 3;

    /**
     * 密码长度
     */
    public static int passwordLength = 8;

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "yeffxoke";


    /**
     * 用户注册
     *
     * @param username      用户名
     * @param password      密码
     * @param checkPassword 校验密码
     * @return 数据库变化条数
     */
    @Override
    public long userRegister(String username, String password, String checkPassword) {
        // 1、校验
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数为空");
        }
        if (username.length() < usernameLength) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号长度过短");
        }
        if (password.length() < passwordLength
                || checkPassword.length() < passwordLength) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码过短");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号中含有特殊字符");
        }
        // 密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "两次密码长度不一致");
        }

        // 账户不能重复
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号已存在");
        }

        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        // queryWrapper.eq("planetCode", email);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "星球编号已存在");
        }
        // 2、加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        // 3、插入数据
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setNickname(username);
        user.setPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "因未知原因导致插入失败");
        }
        return user.getId();
    }

    /**
     * 用户登入
     *
     * @param username     用户账户
     * @param userPassword 用户密码
     * @param request      前端数据
     * @return 脱敏后用户数据
     */
    @Override
    public UserInfo userLogin(String username, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(username, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数为空");
        }
        if (username.length() < usernameLength) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号长度过短");
        }
        if (userPassword.length() < passwordLength) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码长度过短");
        }

        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账户中含有特殊字符");
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", encryptPassword);
        UserInfo user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, username Cannot match userPassword");
            return null;
        }
        // 3.用户脱敏
        UserInfo safetyUser = getSafetyUser(user);
        // 4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser 原始用户
     * @return 脱敏用户
     */
    @Override
    public UserInfo getSafetyUser(UserInfo originUser) {
        if (originUser == null) {
            return null;
        }
        UserInfo safetyUser = new UserInfo();
        safetyUser.setId(originUser.getId());
        safetyUser.setNickname(originUser.getNickname());
        safetyUser.setAvatar(originUser.getAvatar());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request 前端数据
     * @return 退出成功
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登入态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}
