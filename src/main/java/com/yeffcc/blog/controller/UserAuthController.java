package com.yeffcc.blog.controller;

import com.yeffcc.blog.annotation.AccessLimit;
import com.yeffcc.blog.dto.UserAreaDTO;
import com.yeffcc.blog.dto.UserBackDTO;
import com.yeffcc.blog.dto.UserInfoDTO;
import com.yeffcc.blog.service.UserAuthService;
import com.yeffcc.blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户权限控制器
 *
 * @author xoke
 * @date 2022/8/10
 */
@Api(tags = "用户账号模块")
@RestController
public class UserAuthController {

    @Resource
    private UserAuthService userAuthService;

    /**
     * 发送邮箱验证码
     *
     * @param username 用户名
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/users/code")
    public Result<?> sendCode(String username) {
        userAuthService.sendCode(username);
        return Result.ok();
    }

    /**
     * 获取用户区域分布
     *
     * @param conditionVO 条件
     * @return 用户区域分布
     */
    @ApiOperation(value = "获取用户区域分布")
    @GetMapping("/admin/users/area")
    public Result<List<UserAreaDTO>> listUserAreas(ConditionVO conditionVO) {
        return Result.ok(userAuthService.listUserAreas(conditionVO));
    }

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    @ApiOperation(value = "查询后台用户列表")
    @GetMapping("/admin/users")
    public Result<PageResult<UserBackDTO>> listUsers(ConditionVO condition) {
        return Result.ok(userAuthService.listUserBackDTO(condition));
    }

    /**
     * 用户注册
     *
     * @param userVO 用户信息
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody UserVO userVO) {
        userAuthService.register(userVO);
        return Result.ok();
    }

    /**
     * 修改密码
     *
     * @param user 用户信息
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("/users/password")
    public Result<?> updatePassword(@Valid @RequestBody UserVO user) {
        userAuthService.updatePassword(user);
        return Result.ok();
    }

    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码信息
     */
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/users/password")
    public Result<?> updateAdminPassword(@Valid @RequestBody PasswordVO passwordVO) {
        userAuthService.updateAdminPassword(passwordVO);
        return Result.ok();
    }

    /**
     * 微信登录
     *
     * @param weChatLoginVO 微信登录信息
     * @return 用户信息
     */
    @ApiOperation(value = "微信登录")
    @PostMapping("/users/oauth/wechat")
    public Result<UserInfoDTO> weChatLogin(@Valid @RequestBody WeChatLoginVO weChatLoginVO) {
        return Result.ok(userAuthService.weChatLogin(weChatLoginVO));
    }

    /**
     * qq登录
     *
     * @param qqLoginVO qq登录信息
     * @return 用户信息
     */
    @ApiOperation(value = "qq登录")
    @PostMapping("/users/oauth/qq")
    public Result<UserInfoDTO> qqLogin(@Valid @RequestBody QQLoginVO qqLoginVO) {
        return Result.ok(userAuthService.qqLogin(qqLoginVO));
    }

}
