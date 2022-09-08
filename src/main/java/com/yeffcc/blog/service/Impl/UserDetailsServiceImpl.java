package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yeffcc.blog.dto.UserDetailDTO;
import com.yeffcc.blog.entity.UserAuth;
import com.yeffcc.blog.entity.UserInfo;
import com.yeffcc.blog.exception.BusinessException;
import com.yeffcc.blog.mapper.RoleMapper;
import com.yeffcc.blog.mapper.UserAuthMapper;
import com.yeffcc.blog.mapper.UserInfoMapper;
import com.yeffcc.blog.service.RedisService;
import com.yeffcc.blog.util.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.yeffcc.blog.constant.RedisPrefixConst.*;
import static com.yeffcc.blog.enums.ZoneEnum.SHANGHAI;

/**
 * 用户详细信息业务层接口实现类
 *
 * @author xoke
 * @date 2022/9/8
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserAuthMapper userAuthMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private HttpServletRequest request;


    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BusinessException("用户名不能为空！");
        }
        // 查询账号是否存在
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getId, UserAuth::getUserInfoId, UserAuth::getUsername, UserAuth::getPassword, UserAuth::getLoginType)
                .eq(UserAuth::getUsername, username));
        if (Objects.isNull(userAuth)) {
            throw new BusinessException("用户名不存在！");
        }
        // 封装登入信息
        return convertUserDetail(userAuth, request);
    }

    /**
     * 封装用户登入信息
     *
     * @param userAuth 用户账号
     * @param request  前端请求
     * @return 用户登入信息
     */
    public UserDetailDTO convertUserDetail(UserAuth userAuth, HttpServletRequest request) {
        // 查询账号信息
        UserInfo userInfo = userInfoMapper.selectById(userAuth.getUserInfoId());
        // 查询账号角色
        List<String> roleList = roleMapper.listRolesByUserInfoId(userInfo.getId());
        // 查询账号点赞
        Set<Object> articleLikeSet = redisService.sMembers(ARTICLE_USER_LIKE + userInfo.getId());
        Set<Object> commentLikeSet = redisService.sMembers(COMMENT_USER_LIKE + userInfo.getId());
        Set<Object> talkLikeSet = redisService.sMembers(TALK_USER_LIKE + userInfo.getId());
        // 获取设备信息
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        UserAgent userAgent = IpUtils.getUserAgent(request);
        // 封装
        return UserDetailDTO.builder()
                .id(userAuth.getId())
                .userInfoId(userAuth.getUserInfoId())
                .email(userInfo.getEmail())
                .loginType(userAuth.getLoginType())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .roleList(roleList)
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .webSite(userInfo.getWebSite())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .talkLikeSet(talkLikeSet)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .isDisable(userInfo.getIsDisable())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())))
                .build();
    }
}
