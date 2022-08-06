package com.yeffxyz.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.entity.Article;
import com.yeffxyz.blog.entity.Comment;
import com.yeffxyz.blog.entity.Category;

import java.util.List;

/**
 * 博客业务层接口
 *
 * @author xoke
 * @date 2022/7/23
 */
public interface BlogService extends IService<Article> {

    /**
     * 保存新增博客
     *
     * @param article 博客
     * @return 数据库更改的条数
     */
    int saveBlog(Article article);

    /**
     * 删除博客
     *
     * @param id 博客id
     */
    void deleteBlog(Long id);

    /**
     * 查询文章管理列表
     *
     * @return 所有博客
     */
    List<Article> getAllBlog();

    /**
     * 查询编辑修改的文章
     *
     * @param id 博客id
     * @return 该id的博客
     */
    Article getBlogById(Long id);

    /**
     * 编辑修改文章
     *
     * @param article 博客
     * @return 数据库修改的条数
     */
    int updateBlog(Article article);

    /**
     * 搜索博客管理列表
     *
     * @param article
     * @return
     */
    List<Article> searchByTitleAndType(Article article);


    /**
     * 查询首页最新博客列表信息
     *
     * @return
     */
    List<Article> getLastBlog();

    /**
     * 查询首页最新推荐信息
     *
     * @return
     */
    List<Article> getRecommendedBlog();

    /**
     * 搜索博客列表
     *
     * @param query
     * @return
     */
    List<Article> getSearchBlog(String query);

    /**
     * 统计博客总数
     *
     * @return
     */
    Integer getBlogTotal();

    /**
     * 统计访问总数
     *
     * @return
     */
    Integer getBlogViewTotal();

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
