package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.entity.User;
import com.yeffxyz.blog.mapper.UserMapper;
import com.yeffxyz.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
     * 核对管理员用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回用户对象
     */
    @Override
    public User checkUser(String username, String password) {
        // 1.校验
        if (StringUtils.isAnyBlank(username, password)) {
            return null;
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            return null;
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount Cannot match userPassword");
            return null;
        }
        // 3.用户脱敏
        User safetyUser = getSafetyUser(user);
        // 4.记录用户的登录状态
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser 原始数据
     * @return 脱敏后的用户数据
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUsername(originUser.getNickname());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }
}
