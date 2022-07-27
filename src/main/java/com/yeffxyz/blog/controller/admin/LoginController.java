package com.yeffxyz.blog.controller.admin;

import com.yeffxyz.blog.entity.User;
import com.yeffxyz.blog.entity.request.UserRegisterRequest;
import com.yeffxyz.blog.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户登录控制器
 *
 * @author xoke
 * @date 2022/7/27
 */
@RestController
@RequestMapping("/admin")
public class LoginController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public User login(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if (userRegisterRequest == null) {
            return null;
        }

        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();

        if (StringUtils.isAnyBlank(username, password)) {
            return null;
        }
        User user = userService.checkUser(username, password);
        return user;
    }
}
