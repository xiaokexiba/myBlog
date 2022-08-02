package com.yeffxyz.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.entity.Blog;
import com.yeffxyz.blog.entity.Comment;
import com.yeffxyz.blog.entity.Type;

import java.util.List;

/**
 * 博客业务层接口
 *
 * @author xoke
 * @date 2022/7/23
 */
public interface BlogService extends IService<Blog> {

    /**
     * 保存新增博客
     *
     * @param blog 博客
     * @return 数据库更改的条数
     */
    int saveBlog(Blog blog);

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
    List<Blog> getAllBlog();

    /**
     * 查询编辑修改的文章
     *
     * @param id 博客id
     * @return 该id的博客
     */
    Blog getBlogById(Long id);

    /**
     * 编辑修改文章
     *
     * @param blog 博客
     * @return 数据库修改的条数
     */
    int updateBlog(Blog blog);

    /**
     * 搜索博客管理列表
     *
     * @param blog
     * @return
     */
    List<Blog> searchByTitleAndType(Blog blog);


    /**
     * 查询首页最新博客列表信息
     *
     * @return
     */
    List<Blog> getLastBlog();

    /**
     * 查询首页最新推荐信息
     *
     * @return
     */
    List<Blog> getRecommendedBlog();

    /**
     * 搜索博客列表
     *
     * @param query
     * @return
     */
    List<Blog> getSearchBlog(String query);

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
    Blog getDetailedBlog(Long id);

    /**
     * 根据TypeId查询博客列表，显示在分类页面
     *
     * @param typeId
     * @return
     */
    List<Type> getByTypeId(Long typeId);

    /**
     * 查询最新评论
     *
     * @return
     */
    List<Comment> getNewComment();

}
