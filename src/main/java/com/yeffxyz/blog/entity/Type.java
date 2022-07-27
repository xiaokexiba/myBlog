package com.yeffxyz.blog.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客类型实体类
 *
 * @author xoke
 * @date 2022/7/23
 */
@Data
public class Type {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 属于同一类型的博客集合
     */
    private List<Blog> blogs = new ArrayList<>();
}
