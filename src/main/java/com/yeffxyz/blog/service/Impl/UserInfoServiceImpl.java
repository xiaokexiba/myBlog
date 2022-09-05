package com.yeffxyz.blog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.enums.StatusCodeEnum;
import com.yeffxyz.blog.dto.UserDetailDTO;
import com.yeffxyz.blog.dto.UserOnlineDTO;
import com.yeffxyz.blog.entity.UserInfo;
import com.yeffxyz.blog.entity.UserRole;
import com.yeffxyz.blog.enums.FilePathEnum;
import com.yeffxyz.blog.exception.BusinessException;
import com.yeffxyz.blog.mapper.UserInfoMapper;
import com.yeffxyz.blog.service.RedisService;
import com.yeffxyz.blog.service.UserInfoService;
import com.yeffxyz.blog.service.UserRoleService;
import com.yeffxyz.blog.util.UserUtils;
import com.yeffxyz.blog.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.yeffxyz.blog.constant.RedisPrefixConst.USER_CODE_KEY;
import static com.yeffxyz.blog.util.PageUtils.getLimitCurrent;
import static com.yeffxyz.blog.util.PageUtils.getSize;

/**
 * 用户信息业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/9
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RedisService redisService;

    @Resource
    private SessionRegistry sessionRegistry;

    /**
     * 修改用户信息
     *
     * @param userInfoVO 用户信息
     */
    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        // 封装用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId().longValue())
                .nickname(userInfoVO.getNickname())
                .intro(userInfoVO.getIntro())
                .build();
        userInfoMapper.updateById(userInfo);
    }

    /**
     * 修改用户头像
     *
     * @param file 头像图片
     * @return 头像OSS地址
     */
    @Override
    public String updateUserAvatar(MultipartFile file) {
        // 头像上传
        String avatar = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        // 更新用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId().longValue())
                .avatar(avatar)
                .build();
        userInfoMapper.updateById(userInfo);
        return avatar;
    }

    /**
     * 绑定用户邮箱
     *
     * @param emailVO 邮箱
     */
    @Override
    public void saveUserEmail(EmailVO emailVO) {
        if (!emailVO.getCode().equals(redisService.get(USER_CODE_KEY + emailVO.getEmail()).toString())) {
            throw new BusinessException("验证码错误！");
        }
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId().longValue())
                .email(emailVO.getEmail())
                .build();
        userInfoMapper.updateById(userInfo);
    }

    /**
     * 修改用户权限
     *
     * @param userRoleVO 用户权限
     */
    @Override
    public void updateUserRole(UserRoleVO userRoleVO) {
        // 更新用户角色和昵称
        UserInfo userInfo = UserInfo.builder()
                .id(userRoleVO.getUserInfoId().longValue())
                .nickname(userRoleVO.getNickname())
                .build();
        userInfoMapper.updateById(userInfo);
        // 删除用户角色重新添加
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleVO.getUserInfoId()));
        List<UserRole> userRoleList = userRoleVO.getRoleIdList().stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleVO.getUserInfoId())
                        .build())
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    /**
     * 修改用户禁用状态
     *
     * @param userDisableVO 用户禁用信息
     */
    @Override
    public void updateUserDisable(UserDisableVO userDisableVO) {
        // 更新用户禁用状态
        UserInfo userInfo = UserInfo.builder()
                .id(userDisableVO.getId().longValue())
                .isDisable(userDisableVO.getIsDisable())
                .build();
        userInfoMapper.updateById(userInfo);
    }

    /**
     * 查看在线用户列表
     *
     * @param conditionVO 条件
     * @return 在线用户列表
     */
    @Override
    public PageResult<UserOnlineDTO> listOnlineUsers(ConditionVO conditionVO) {
        // 获取security在线session
        List<UserOnlineDTO> userOnlineDTOList = sessionRegistry.getAllPrincipals().stream()
                .filter(item -> sessionRegistry.getAllSessions(item, false).size() > 0)
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserOnlineDTO.class))
                .filter(item -> StringUtils.isBlank(conditionVO.getKeywords()) || item.getNickname().contains(conditionVO.getKeywords()))
                .sorted(Comparator.comparing(UserOnlineDTO::getLastLoginTime).reversed())
                .collect(Collectors.toList());
        // 执行分页
        int fromIndex = getLimitCurrent().intValue();
        int size = getSize().intValue();
        int toIndex = userOnlineDTOList.size() - fromIndex > size ? fromIndex + size : userOnlineDTOList.size();
        List<UserOnlineDTO> userOnlineList = userOnlineDTOList.subList(fromIndex, toIndex);
        return new PageResult<>(userOnlineList, userOnlineDTOList.size());
    }

    /**
     * 下线用户
     *
     * @param userInfoId 用户信息id
     */
    @Override
    public void removeOnlineUser(Integer userInfoId) {
        // 获取用户session
        List<Object> userInfoList = sessionRegistry.getAllPrincipals().stream().filter(item -> {
            UserDetailDTO userDetailDTO = (UserDetailDTO) item;
            return userDetailDTO.getUserInfoId().equals(userInfoId);
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        userInfoList.forEach(item -> allSessions.addAll(sessionRegistry.getAllSessions(item, false)));
        // 注销session
        allSessions.forEach(SessionInformation::expireNow);
    }
}
