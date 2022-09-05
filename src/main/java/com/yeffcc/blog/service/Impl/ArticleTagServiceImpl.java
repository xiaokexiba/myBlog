package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.mapper.ArticleTagMapper;
import com.yeffcc.blog.service.ArticleTagService;
import com.yeffcc.blog.entity.ArticleTag;
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




