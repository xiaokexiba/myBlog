package com.yeffxyz.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 博客实体类
 *
 * @author xoke
 * @date 2022/7/23
 */
@Data
@TableName(value = "t_blog")
public class Blog {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 博客标题
     */
    private String title;
    /**
     * 博客内容
     */
    private String content;
    /**
     * 首页图片
     */
    private String firstPicture;
    /**
     * 标签
     */
    private String flag;
    /**
     * 浏览次数
     */
    private Integer views;
    /**
     * 是否开启赞赏
     */
    private boolean appreciation;
    /**
     * 是否开启版权
     */
    private boolean copyright;
    /**
     * 是否开启评论
     */
    private boolean commentable;
    /**
     * 是否发布
     */
    private boolean published;
    /**
     * 是否推荐
     */
    public boolean recommend;
    /**
     * 评论次数
     */
    public Integer commentCount;
    /**
     * 博客最初的创建时间
     */
    private Date createTime;
    /**
     * 博客最近一次修改时间
     */
    private Date updateTime;
    /**
     * 博客描述
     */
    private String description;

    /**
     * 博客所属类型
     */
    private Type type;
    private User user;
    /**
     * 博客类型id
     */
    private Long typeId;
    private Long userId;
    /**
     * 博客下的所有评论集合
     */
    private List<Comment> comments = new ArrayList<>();

}
