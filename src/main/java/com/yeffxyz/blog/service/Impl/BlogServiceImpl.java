package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.common.MarkdownUtils;
import com.yeffxyz.blog.common.ResultCode;
import com.yeffxyz.blog.entity.Blog;
import com.yeffxyz.blog.entity.Comment;
import com.yeffxyz.blog.entity.Type;
import com.yeffxyz.blog.exception.BusinessException;
import com.yeffxyz.blog.service.BlogService;
import com.yeffxyz.blog.mapper.BlogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 博客业务层接口实现类
 *
 * @author xoke
 * @date 2022/7/30
 */
@Service
@Slf4j
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Resource
    private BlogMapper blogMapper;

    /**
     * 保存新增博客
     *
     * @param blog 博客
     * @return 数据库更改的条数
     */
    @Override
    public int saveBlog(Blog blog) {
        Date now = new Date();
        blog.setCreateTime(now);
        blog.setUpdateTime(now);
        blog.setViews(0);
        blog.setCommentCount(0);
        return blogMapper.insert(blog);
    }

    /**
     * 删除博客
     *
     * @param id 博客id
     */
    @Override
    public void deleteBlog(Long id) {
        blogMapper.deleteById(id);
    }

    /**
     * 查询文章管理列表
     *
     * @return 文章管理列表
     */
    @Override
    public List<Blog> getAllBlog() {
        return blogMapper.getAllBlog();
    }

    /**
     * 查询编辑修改的文章
     *
     * @param id 博客id
     * @return 编辑修改的博客
     */
    @Override
    public Blog getBlogById(Long id) {
        return blogMapper.selectById(id);
    }

    /**
     * 编辑修改文章
     *
     * @param blog 需要修改的博客
     * @return 数据库变化条数
     */
    @Override
    public int updateBlog(Blog blog) {
        return blogMapper.updateById(blog);
    }

    /**
     * 搜索博客管理列表
     *
     * @param blog 查询的博客
     * @return 博客管理列表
     */
    @Override
    public List<Blog> searchByTitleAndType(Blog blog) {
        return blogMapper.searchByTitleAndType(blog);
    }

    /**
     * 查询首页最新博客列表信息
     *
     * @return 首页最新博客列表信息
     */
    @Override
    public List<Blog> getLastBlog() {
        return blogMapper.getLastBlog();
    }

    /**
     * 查询首页最新推荐信息
     *
     * @return 首页最新推荐信息
     */
    @Override
    public List<Blog> getRecommendedBlog() {
        return blogMapper.getAllRecommendBlog();
    }

    /**
     * 搜索博客列表
     *
     * @param query 查询关键字
     * @return 含有该内容的博客列表
     */
    @Override
    public List<Blog> getSearchBlog(String query) {
        return blogMapper.getSearchBlog(query);
    }

    /**
     * 统计博客总数
     *
     * @return 博客总数
     */
    @Override
    public Integer getBlogTotal() {
        return blogMapper.getAllBlog().size();
    }

    /**
     * 查看浏览量
     *
     * @return 浏览量
     */
    @Override
    public Integer getBlogViewTotal() {
        return blogMapper.getBlogViewTotal();
    }

    /**
     * 统计评论总数
     *
     * @return 评论总数
     */
    @Override
    public Integer getBlogCommentTotal() {
        return blogMapper.getBlogCommentTotal();
    }

    /**
     * 统计留言总数
     *
     * @return 留言总数
     */
    @Override
    public Integer getBlogMessageTotal() {
        return blogMapper.getBlogMessageTotal();
    }

    /**
     * 查询博客详情
     *
     * @param id 博客id
     * @return 返回博客的详情
     */
    @Override
    public Blog getDetailedBlog(Long id) {
        Blog detailedBlog = blogMapper.getDetailedBlog(id);
        if (detailedBlog == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该博客不存在");
        }
        String content = detailedBlog.getContent();
        detailedBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        //文章访问数量自增
        blogMapper.updateViews(id);
        //文章评论数量更新
        blogMapper.getCommentCountById(id);
        return detailedBlog;
    }

    /**
     * 根据TypeId查询博客列表，显示在分类页面
     *
     * @param typeId 类型id
     * @return id所属分类
     */
    @Override
    public List<Type> getByTypeId(Long typeId) {
        return blogMapper.getByTypeId(typeId);
    }

    /**
     * 查询最新评论
     *
     * @return 最新的评论列表
     */
    @Override
    public List<Comment> getNewComment() {
        return null;
    }
}




