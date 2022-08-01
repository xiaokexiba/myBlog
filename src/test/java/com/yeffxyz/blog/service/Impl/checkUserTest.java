package com.yeffxyz.blog.service.Impl;

import com.yeffxyz.blog.entity.User;
import com.yeffxyz.blog.service.UserService;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 测试登入
 *
 * @author xoke
 * @date 2022/7/30
 */
public class checkUserTest {
    @Resource
    private UserService userService;

    @Test
    public void check(){
        User user = new User();
        user.setNickname("xiaoke");
        user.setUsername("hhh");
        user.setPassword("123123123");
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        user.setEmail("yjm@126.com");
        user.setType(1);
        user.setAvatar("https://lh3.googleusercontent.com/wJnjWz3kB_kUYn8ii8xyV_Gzo03XdzLPFg9zCQFDS0NM05ghIwR_IDOI4tkqoDaEGHQDyWLFtioAwHDHTM4Oah_O");
        System.out.println(userService.userLogin("hhh","123123123"));

    }

}
