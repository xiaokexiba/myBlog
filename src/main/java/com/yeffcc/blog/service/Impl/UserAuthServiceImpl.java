package com.yeffcc.blog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.dto.EmailDTO;
import com.yeffcc.blog.enums.LoginTypeEnum;
import com.yeffcc.blog.service.BlogInfoService;
import com.yeffcc.blog.constant.CommonConst;
import com.yeffcc.blog.dto.UserAreaDTO;
import com.yeffcc.blog.dto.UserBackDTO;
import com.yeffcc.blog.dto.UserInfoDTO;
import com.yeffcc.blog.entity.UserAuth;
import com.yeffcc.blog.entity.UserInfo;
import com.yeffcc.blog.entity.UserRole;
import com.yeffcc.blog.exception.BusinessException;
import com.yeffcc.blog.mapper.UserAuthMapper;
import com.yeffcc.blog.mapper.UserInfoMapper;
import com.yeffcc.blog.mapper.UserRoleMapper;
import com.yeffcc.blog.service.RedisService;
import com.yeffcc.blog.service.UserAuthService;
import com.yeffcc.blog.strategy.context.SocialLoginStrategyContext;
import com.yeffcc.blog.util.PageUtils;
import com.yeffcc.blog.util.UserUtils;
import com.yeffcc.blog.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yeffcc.blog.constant.CommonConst.*;
import static com.yeffcc.blog.constant.MQPrefixConst.EMAIL_EXCHANGE;
import static com.yeffcc.blog.constant.RedisPrefixConst.*;
import static com.yeffcc.blog.enums.RoleEnum.USER;
import static com.yeffcc.blog.enums.UserAreaTypeEnum.getUserAreaType;
import static com.yeffcc.blog.util.CommonUtils.checkEmail;
import static com.yeffcc.blog.util.CommonUtils.getRandomCode;

/**
 * ????????????????????????????????????
 *
 * @author xoke
 * @date 2022/8/10
 */
@Slf4j
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    @Resource
    private RedisService redisService;
    @Resource
    private UserAuthMapper userAuthMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private SocialLoginStrategyContext socialLoginStrategyContext;

    /**
     * ???????????????
     *
     * @param emailName ?????????
     */
    @Override
    public void sendCode(String emailName) {
        // ????????????????????????
        if (!checkEmail(emailName)) {
            throw new BusinessException("?????????????????????");
        }
        // ?????????????????????????????????
        String code = getRandomCode();
        // ???????????????
        EmailDTO emailDTO = EmailDTO.builder()
                .email(emailName)
                .subject("?????????")
                .content("?????????????????? " + code + " ?????????15????????????????????????????????????")
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        // ??????????????????redis????????????????????????15??????
        redisService.set(USER_CODE_KEY + emailName, code, CODE_EXPIRE_TIME);
    }

    /**
     * ????????????????????????
     *
     * @param conditionVO ??????
     * @return ??????????????????
     */
    @Override
    public List<UserAreaDTO> listUserAreas(ConditionVO conditionVO) {
        List<UserAreaDTO> userAreaDTOList = new ArrayList<>();
        switch (Objects.requireNonNull(getUserAreaType(conditionVO.getType()))) {
            case USER:
                // ??????????????????????????????
                Object userArea = redisService.get(USER_AREA);
                if (Objects.nonNull(userArea)) {
                    userAreaDTOList = JSON.parseObject(userArea.toString(), List.class);
                }
                return userAreaDTOList;
            case VISITOR:
                // ????????????????????????
                Map<String, Object> visitorArea = redisService.hGetAll(VISITOR_AREA);
                if (Objects.nonNull(visitorArea)) {
                    userAreaDTOList = visitorArea.entrySet().stream()
                            .map(item -> UserAreaDTO.builder()
                                    .name(item.getKey())
                                    .value(Long.valueOf(item.getValue().toString()))
                                    .build())
                            .collect(Collectors.toList());
                }
                return userAreaDTOList;
            default:
                break;
        }
        return userAreaDTOList;
    }

    /**
     * ????????????
     *
     * @param user ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UserVO user) {
        // ????????????????????????
        if (checkUser(user)) {
            throw new BusinessException("?????????????????????");
        }
        // ??????????????????
        UserInfo userInfo = UserInfo.builder()
                .email(user.getUsername())
                .nickname(CommonConst.DEFAULT_NICKNAME + IdWorker.getId())
                .avatar(blogInfoService.getWebsiteConfig().getUserAvatar())
                .build();
        userInfoMapper.insert(userInfo);
        // ??????????????????
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
        // ??????????????????
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(user.getUsername())
                .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .loginType(LoginTypeEnum.EMAIL.getType())
                .build();
        userAuthMapper.insert(userAuth);
    }

    /**
     * ????????????
     *
     * @param userVO ????????????
     */
    @Override
    public void login(UserVO userVO) {
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUsername, userVO.getUsername())
                .eq(UserAuth::getPassword, userVO.getPassword()));
        if (userAuth == null) {
            throw new BusinessException("????????????????????????????????????????????????");
        }
    }

    /**
     * ????????????
     *
     * @param userVO ????????????
     */
    @Override
    public void updatePassword(UserVO userVO) {
        // ????????????????????????
        if (!checkUser(userVO)) {
            throw new BusinessException("?????????????????????");
        }
        // ???????????????????????????
        userAuthMapper.update(new UserAuth(), new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getPassword, BCrypt.hashpw(userVO.getPassword(), BCrypt.gensalt()))
                .eq(UserAuth::getUsername, userVO.getUsername()));
    }

    /**
     * ?????????????????????
     *
     * @param passwordVO ????????????
     */
    @Override
    public void updateAdminPassword(PasswordVO passwordVO) {
        // ???????????????????????????
        UserAuth user = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getId, UserUtils.getLoginUser().getId()));
        // ????????????????????????????????????????????????
        if (Objects.nonNull(user) && BCrypt.checkpw(passwordVO.getOldPassword(), user.getPassword())) {
            UserAuth userAuth = UserAuth.builder()
                    .id(UserUtils.getLoginUser().getId())
                    .password(BCrypt.hashpw(passwordVO.getNewPassword(), BCrypt.gensalt()))
                    .build();
            userAuthMapper.updateById(userAuth);
        } else {
            throw new BusinessException("??????????????????");
        }
    }

    /**
     * ????????????????????????
     *
     * @param conditionVO ??????
     * @return ????????????
     */
    @Override
    public PageResult<UserBackDTO> listUserBackDTO(ConditionVO conditionVO) {
        // ????????????????????????
        Integer count = userAuthMapper.countUser(conditionVO);
        if (count == 0) {
            return new PageResult<>();
        }
        // ????????????????????????
        List<UserBackDTO> userBackDTOList = userAuthMapper.listUsers(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        return new PageResult<>(userBackDTOList, count);
    }

    /**
     * QQ??????
     *
     * @param qqLoginVO qq????????????
     * @return ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoDTO qqLogin(QQLoginVO qqLoginVO) {
        return socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(qqLoginVO), LoginTypeEnum.QQ);
    }

    /**
     * ????????????
     *
     * @param weChatLoginVO ??????????????????
     * @return ????????????
     */
    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public UserInfoDTO weChatLogin(WeChatLoginVO weChatLoginVO) {
        return socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(weChatLoginVO), LoginTypeEnum.WECHAT);
    }

    /**
     * ??????????????????????????????
     *
     * @param user ????????????
     * @return ??????
     */
    private Boolean checkUser(UserVO user) {
        if (!user.getCode().equals(redisService.get(USER_CODE_KEY + user.getUsername()))) {
            throw new BusinessException("??????????????????");
        }
        //???????????????????????????
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUsername)
                .eq(UserAuth::getUsername, user.getUsername()));
        return Objects.nonNull(userAuth);
    }

    /**
     * ??????????????????
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void statisticalUserArea() {
        // ????????????????????????
        Map<String, Long> userAreaMap = userAuthMapper.selectList(new LambdaQueryWrapper<UserAuth>().select(UserAuth::getIpSource))
                .stream()
                .map(item -> {
                    if (StringUtils.isNotBlank(item.getIpSource())) {
                        return item.getIpSource().substring(0, 2)
                                .replaceAll(PROVINCE, "")
                                .replaceAll(CITY, "");
                    }
                    return UNKNOWN;
                })
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        // ????????????
        List<UserAreaDTO> userAreaList = userAreaMap.entrySet().stream()
                .map(item -> UserAreaDTO.builder()
                        .name(item.getKey())
                        .value(item.getValue())
                        .build())
                .collect(Collectors.toList());
        redisService.set(USER_AREA, JSON.toJSONString(userAreaList));
    }

}
