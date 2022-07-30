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

}




