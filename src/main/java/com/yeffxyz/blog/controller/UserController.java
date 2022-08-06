package com.yeffxyz.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeffxyz.blog.common.BaseResponse;
import com.yeffxyz.blog.common.ResultCode;
import com.yeffxyz.blog.common.ResultUtils;
import com.yeffxyz.blog.entity.UserInfo;
import com.yeffxyz.blog.entity.request.UserRegisterRequest;
import com.yeffxyz.blog.exception.BusinessException;
import com.yeffxyz.blog.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.yeffxyz.blog.constant.UserConstant.ADMIN_ROLE;
import static com.yeffxyz.blog.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户登录控制器
 *
 * @author xoke
 * @date 2022/7/27
 */
@RestController
@RequestMapping("/user")
public class UserController {
}