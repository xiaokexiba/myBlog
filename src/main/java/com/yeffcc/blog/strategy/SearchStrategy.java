package com.yeffcc.blog.strategy;

import com.yeffcc.blog.dto.ArticleSearchDTO;

import java.util.List;

/**
 * 搜索策略
 *
 * @author xoke
 * @date 2022/9/12
 */
public interface SearchStrategy {

    /**
     * 搜索文章
     *
     * @param keywords 关键词
     * @return 文章列表
     */
    List<ArticleSearchDTO> searchArticle(String keywords);
}
