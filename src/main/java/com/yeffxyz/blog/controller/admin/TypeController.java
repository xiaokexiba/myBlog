package com.yeffxyz.blog.controller.admin;

import com.yeffxyz.blog.service.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 分类控制器
 *
 * @author xoke
 * @date 2022/7/28
 */
@RestController
@RequestMapping("/admin")
public class TypeController {
    @Resource
    private TypeService typeService;

    @GetMapping("/types")
    public String types() {
        return "admin/types";
    }
}
