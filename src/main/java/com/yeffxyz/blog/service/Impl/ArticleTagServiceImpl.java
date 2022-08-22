package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.entity.ArticleTag;
import com.yeffxyz.blog.service.ArticleTagService;
import com.yeffxyz.blog.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
 * 文章标签业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/22
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
        implements ArticleTagService {

}




