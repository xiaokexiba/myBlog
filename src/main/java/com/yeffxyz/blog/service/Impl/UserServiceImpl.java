package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.common.ResultCode;
import com.yeffxyz.blog.entity.User;
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

import static com.sun.javafx.font.FontResource.SALT;

/**
 * 用户业务层接口实现类
 *
 * @author xoke
 * @date 2022/7/27
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 用户名长度
     */
    public static int userAccountLength = 4;

    /**
     * 密码长度
     */
    public static int userPasswordLength = 8;

    /**
     * 星球编号长度
     */
    public static int planetCodeLength = 5;

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "xiaoke";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < userAccountLength) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号长度过短");
        }
        if (userPassword.length() < userPasswordLength
                || checkPassword.length() < userPasswordLength) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码过短");
        }

        if (planetCode.length() > planetCodeLength) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "星球编号过长");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号中含有特殊字符");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "两次密码长度不一致");
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号已存在");
        }

        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "星球编号已存在");
        }
        // 2、加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        // 3、插入数据
        User user = new User();
        user.setUsername(userAccount);
        user.setNickname(userAccount);
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
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户数据
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < userAccountLength) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号长度过短");
        }
        if (userPassword.length() < userPasswordLength) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码长度过短");
        }

        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账户中含有特殊字符");
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount Cannot match userPassword");
            return null;
        }
        // 3.用户脱敏
        User safetyUser = getSafetyUser(user);
        // 4.记录用户的登录状态
        request.getSession().setAttribute("USER_LOGIN_STATE", safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setNickname(originUser.getNickname());
        safetyUser.setAvatar(originUser.getAvatar());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登入态
        request.getSession().removeAttribute("USER_LOGIN_STATE");
        return 1;
    }
}
