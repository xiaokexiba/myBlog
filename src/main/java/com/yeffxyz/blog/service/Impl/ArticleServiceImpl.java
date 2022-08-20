package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.dto.*;
import com.yeffxyz.blog.entity.Article;
import com.yeffxyz.blog.mapper.ArticleMapper;
import com.yeffxyz.blog.service.ArticleService;
import com.yeffxyz.blog.vo.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/20
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    /**
     * 查询文章归档
     *
     * @return 文章归档
     */
    @Override
    public PageResult<ArchiveDTO> listArchives() {
        return null;
    }

    /**
     * 查询后台文章
     *
     * @param conditionVO 查询条件
     * @return 文章列表
     */
    @Override
    public PageResult<ArticleBackDTO> listArticleBacks(ConditionVO conditionVO) {
        return null;
    }

    /**
     * 查询首页文章
     *
     * @return 文章列表
     */
    @Override
    public List<ArticleHomeDTO> listArticles() {
        return null;
    }

    /**
     * 根据条件查询文章列表
     *
     * @param conditionVO 查询条件
     * @return 文章列表
     */
    @Override
    public ArticlePreviewListDTO listArticleByCondition(ConditionVO conditionVO) {
        return null;
    }

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return 文章列表
     */
    @Override
    public ArticleVO getArticleBackById(Integer articleId) {
        return null;
    }

    /**
     * 根据id查看文章
     *
     * @param articleId 文章id
     * @return 文章列表
     */
    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        return null;
    }

    /**
     * 点赞文章
     *
     * @param articleId 文章id
     */
    @Override
    public void saveArticleLike(Integer articleId) {

    }

    /**
     * 添加或修改文章
     *
     * @param articleVO 文章信息
     */
    @Override
    public void saveOrUpdateArticle(ArticleVO articleVO) {

    }

    /**
     * 修改文章置顶
     *
     * @param articleTopVO 文章置顶信息
     */
    @Override
    public void updateArticleTop(ArticleTopVO articleTopVO) {

    }

    /**
     * 删除或恢复文章
     *
     * @param deleteVO 逻辑删除对象
     */
    @Override
    public void updateArticleDelete(DeleteVO deleteVO) {

    }

    /**
     * 物理删除文章
     *
     * @param articleIdList 文章id集合
     */
    @Override
    public void deleteArticles(List<Integer> articleIdList) {

    }

    /**
     * 导出文章
     *
     * @param articleIdList 文章id列表
     * @return {@link List}<{@link String}> 文件地址
     */
    @Override
    public List<String> exportArticles(List<Integer> articleIdList) {
        return null;
    }
}
