package com.yeffxyz.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.entity.Blog;

/**
 * 博客业务层接口
 *
 * @author xoke
 * @date 2022/7/23
 */
public interface BlogService extends IService<Blog> {

    /**
     * 保存新增博客
     *
     * @param blog 博客
     * @return 数据库更改的条数
     */
    int saveBlog(Blog blog);

    /**
     * 删除博客
     *
     * @param id 博客id
     */
    void deleteBlog(Long id);
}
