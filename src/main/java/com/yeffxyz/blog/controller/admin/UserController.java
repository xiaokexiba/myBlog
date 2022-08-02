package com.yeffxyz.blog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeffxyz.blog.common.BaseResponse;
import com.yeffxyz.blog.common.ResultCode;
import com.yeffxyz.blog.common.ResultUtils;
import com.yeffxyz.blog.entity.User;
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
    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 前端返回的用户数据
     * @return 数据库变化条数
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        // 接受到前端发送的账户
        String username = userRegisterRequest.getUsername();
        // 接受到前端发送的密码
        String userPassword = userRegisterRequest.getPassword();
        // 接受到前端发送的确认密码
        String checkPassword = userRegisterRequest.getCheckPassword();

        // 判断是否有变量为空
        if (StringUtils.isAnyBlank(username, userPassword, checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        // 返回创建用户时返回的编码
        long result = userService.userRegister(username, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登入
     *
     * @param userRegisterRequest 前端返回的用户数据
     * @param request             前端数据
     * @return 脱敏后用户数据
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();

        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(username, password, request);
        return ResultUtils.success(user);

    }

    /**
     * 用户退出
     *
     * @param request 前端数据
     * @return 退出成功
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户数据
     *
     * @param request 前端数据
     * @return 脱敏后用户数据
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ResultCode.NOT_LOGIN);
        }
        long id = currentUser.getId();
        // todo 校验用户是否合法
        User user = userService.getById(id);
        User safeUser = userService.getSafetyUser(user);
        return ResultUtils.success(safeUser);
    }

    /**
     * 查找含有该用户名的用户名
     *
     * @param username 用户名
     * @param request  前端数据
     * @return 用户列表
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        // 搜索要进行脱敏
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 删除用户
     *
     * @param id      用户id
     * @param request 前端数据
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        // 先验证是否为管理员
        if (!isAdmin(request)) {
            // 不是直接抛异常
            throw new BusinessException(ResultCode.NO_AUTH);
        }
        // 其次id要在在正常范围内
        if (id <= 0) {
            // 不是直接抛异常
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        return userService.removeById(id);
    }


    /**
     * 是否为管理员
     *
     * @param request 前端数据
     * @return 是否为管理员
     */
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可用操作
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getType() == ADMIN_ROLE;
    }
}