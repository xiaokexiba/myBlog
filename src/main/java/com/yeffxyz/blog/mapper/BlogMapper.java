package com.yeffxyz.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeffxyz.blog.entity.Article;
import com.yeffxyz.blog.entity.Comment;
import com.yeffxyz.blog.entity.Category;

import java.util.List;

/**
 * 博客持久层接口
 *
 * @author xoke
 * @date 2022/7/23
 */
public interface BlogMapper extends BaseMapper<Article> {

    /**
     * 查询首页最新推荐信息
     *
     * @return 最新的博客列表
     */
    List<Article> getLastBlog();

    /**
     * 查询文章管理列表
     *
     * @return 所有博客列表
     */
    List<Article> getAllBlog();

    /**
     * 查询推荐文章
     *
     * @return 推荐博客列表
     */
    List<Article> getAllRecommendBlog();

    /**
     * 查看浏览量
     *
     * @return
     */
    Integer getBlogViewTotal();

    /**
     * 搜索博客管理列表
     *
     * @param article
     * @return
     */
    List<Article> searchByTitleAndType(Article article);

    /**
     * 搜索博客列表
     *
     * @param query
     * @return
     */
    List<Article> getSearchBlog(String query);

    /**
     * 统计评论总数
     *
     * @return
     */
    Integer getBlogCommentTotal();

    /**
     * 统计留言总数
     *
     * @return
     */
    Integer getBlogMessageTotal();

    /**
     * 查询博客详情
     *
     * @param id
     * @return
     */
    Article getDetailedBlog(Long id);

    /**
     * 文章访问更新
     *
     * @param id
     * @return
     */
    int updateViews(Long id);

    /**
     * 根据博客id查询出评论数量
     *
     * @param id
     * @return
     */
    int getCommentCountById(Long id);


    /**
     * 根据TypeId查询博客列表，显示在分类页面
     *
     * @param typeId
     * @return
     */
    List<Category> getByTypeId(Long typeId);

    /**
     * 查询最新评论
     *
     * @return
     */
    List<Comment> getNewComment();
}




