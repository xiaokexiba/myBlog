package com.yeffxyz.blog.service;

import com.yeffxyz.blog.dto.CommentDTO;
import com.yeffxyz.blog.dto.ReplyDTO;
import com.yeffxyz.blog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.vo.CommentVO;
import com.yeffxyz.blog.vo.ConditionVO;
import com.yeffxyz.blog.vo.PageResult;
import com.yeffxyz.blog.vo.ReviewVO;

import java.util.List;

/**
 * 评论业务层接口
 *
 * @author xoke
 * @date 2022/9/5
 */
public interface CommentService extends IService<Comment> {

    /**
     * 查看评论
     *
     * @param commentVO 评论信息
     * @return 评论列表
     */
    PageResult<CommentDTO> listComment(CommentVO commentVO);

    /**
     * 查看评论下的回复
     *
     * @param commentId 评论id
     * @return 回复列表
     */
    List<ReplyDTO> listReplyByCommentId(Integer commentId);

    /**
     * 添加评论
     *
     * @param commentVO 评论对象
     */
    void saveComment(CommentVO commentVO);

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     */
    void saveCommentLike(Integer commentId);

    /**
     * 审核评论
     *
     * @param reviewVO 审核对象
     */
    void updateCommentsReview(ReviewVO reviewVO);

    /**
     * 查询后台评论
     *
     * @param conditionVO 条件
     * @return 评论列表
     */
    PageResult<CommentBackDTO> listCommentBackDTO(ConditionVO conditionVO);

}
