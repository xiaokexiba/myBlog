package com.yeffxyz.blog.mapper;

import com.yeffxyz.blog.dto.*;
import com.yeffxyz.blog.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeffxyz.blog.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章持久层接口
 *
 * @author xoke
 * @date 2022/8/19
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询首页文章
     *
     * @param current 页码
     * @param size    大小
     * @return 文章列表
     */
    List<ArticleHomeDTO> listArticles(@Param("current") Long current, @Param("size") Long size);

    /**
     * 根据id查询文章
     *
     * @param articleId 文章id
     * @return 文章信息
     */
    ArticleDTO getArticleById(@Param("articleId") Integer articleId);

    /**
     * 根据条件查询文章
     *
     * @param current     页码
     * @param size        大小
     * @param conditionVO 条件
     * @return 文章列表
     */
    List<ArticlePreviewDTO> listArticlesByCondition(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO conditionVO);

    /**
     * 查询后台文章
     *
     * @param current     页码
     * @param size        大小
     * @param conditionVO 条件
     * @return 文章列表
     */
    List<ArticleBackDTO> listArticleBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO conditionVO);

    /**
     * 查询后台文章总量
     *
     * @param conditionVO 条件
     * @return 文章总量
     */
    Integer countArticleBacks(@Param("condition") ConditionVO conditionVO);

    /**
     * 查看文章的推荐文章
     *
     * @param articleId 文章id
     * @return 文章列表
     */
    List<ArticleRecommendDTO> listRecommendArticles(@Param("articleId") Integer articleId);

    /**
     * 文章统计
     *
     * @return {@link List <ArticleStatisticsDTO>} 文章统计结果
     */
    List<ArticleStatisticsDTO> listArticleStatistics();

}




