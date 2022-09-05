package com.yeffcc.blog.util;

import com.yeffcc.blog.dto.UserDetailDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 用户工具类
 *
 * @author xoke
 * @date 2022/8/9
 */
@Component
public class UserUtils {

    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    public static UserDetailDTO getLoginUser() {
        return (UserDetailDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
