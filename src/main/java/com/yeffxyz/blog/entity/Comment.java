package com.yeffxyz.blog.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户评论实体类
 *
 * @author xoke
 * @date 2022/7/23
 */
@Data
public class Comment {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 评论时间
     */
    private Date createTime;
    private Long blogId;
    private Long parentCommentId;
    /**
     * 是否为管理员评论
     */
    private boolean adminComment;
    /**
     * 评论集合
     */
    private List<Comment> replyComments = new ArrayList<>();
    /**
     * 父评论
     */
    private Comment parentComment;
    /**
     * 父评论的昵称
     */
    private String parentNickname;
}
