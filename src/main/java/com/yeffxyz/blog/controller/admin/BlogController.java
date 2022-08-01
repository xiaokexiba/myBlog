package com.yeffxyz.blog.controller.admin;

import com.yeffxyz.blog.common.BaseResponse;
import com.yeffxyz.blog.service.BlogService;
import com.yeffxyz.blog.service.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 博客控制器
 *
 * @author xoke
 * @date 2022/8/1
 */
@RestController
@RequestMapping("/admin")
public class BlogController {
    @Resource
    private BlogService blogService;
    @Resource
    private TypeService typeService;

    @GetMapping("/")
    public BaseResponse blogs() {
        return new BaseResponse();
    }
}
