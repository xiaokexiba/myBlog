package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.entity.Blog;
import com.yeffxyz.blog.service.BlogService;
import com.yeffxyz.blog.mapper.BlogMapper;
import org.springframework.stereotype.Service;

/**
 * 博客业务层接口实现类
 *
 * @author xoke
 * @date 2022/7/30
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    /**
     * 保存新增博客
     *
     * @param blog 博客
     * @return 数据库更改的条数
     */
    @Override
    public int saveBlog(Blog blog) {
        return 0;
    }

    /**
     * 删除博客
     *
     * @param id 博客id
     */
    @Override
    public void deleteBlog(Long id) {

    }
}




