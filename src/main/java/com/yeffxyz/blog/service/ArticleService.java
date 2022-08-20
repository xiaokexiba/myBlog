package com.yeffxyz.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.dto.*;
import com.yeffxyz.blog.entity.Article;
import com.yeffxyz.blog.vo.*;

import java.util.List;

/**
 * 文章业务层接口
 *
 * @author xoke
 * @date 2022/8/20
 */
public interface ArticleService extends IService<Article> {

    /**
     * 查询文章归档
     *
     * @return 文章归档
     */
    PageResult<ArchiveDTO> listArchives();

    /**
     * 查询后台文章
     *
     * @param conditionVO 查询条件
     * @return 文章列表
     */
    PageResult<ArticleBackDTO> listArticleBacks(ConditionVO conditionVO);

    /**
     * 查询首页文章
     *
     * @return 文章列表
     */
    List<ArticleHomeDTO> listArticles();

    /**
     * 根据条件查询文章列表
     *
     * @param conditionVO 查询条件
     * @return 文章列表
     */
    ArticlePreviewListDTO listArticleByCondition(ConditionVO conditionVO);

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return 文章列表
     */
    ArticleVO getArticleBackById(Integer articleId);

    /**
     * 根据id查看文章
     *
     * @param articleId 文章id
     * @return 文章列表
     */
    ArticleDTO getArticleById(Integer articleId);

    /**
     * 点赞文章
     *
     * @param articleId 文章id
     */
    void saveArticleLike(Integer articleId);

    /**
     * 添加或修改文章
     *
     * @param articleVO 文章信息
     */
    void saveOrUpdateArticle(ArticleVO articleVO);

    /**
     * 修改文章置顶
     *
     * @param articleTopVO 文章置顶信息
     */
    void updateArticleTop(ArticleTopVO articleTopVO);

    /**
     * 删除或恢复文章
     *
     * @param deleteVO 逻辑删除对象
     */
    void updateArticleDelete(DeleteVO deleteVO);

    /**
     * 物理删除文章
     *
     * @param articleIdList 文章id集合
     */
    void deleteArticles(List<Integer> articleIdList);

    /**
     * 导出文章
     *
     * @param articleIdList 文章id列表
     * @return {@link List}<{@link String}> 文件地址
     */
    List<String> exportArticles(List<Integer> articleIdList);

}
