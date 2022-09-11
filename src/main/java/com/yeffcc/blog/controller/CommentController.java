package com.yeffcc.blog.controller;

import com.yeffcc.blog.annotation.OptLog;
import com.yeffcc.blog.dto.CommentBackDTO;
import com.yeffcc.blog.dto.CommentDTO;
import com.yeffcc.blog.dto.ReplyDTO;
import com.yeffcc.blog.mapper.CommentMapper;
import com.yeffcc.blog.service.CommentService;
import com.yeffcc.blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apiguardian.api.API;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.yeffcc.blog.constant.OptTypeConst.UPDATE;

/**
 * 评论控制器
 *
 * @author xoke
 * @date 2022/9/11
 */
@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 查看评论
     *
     * @param commentVO 评论信息
     * @return 评论列表
     */
    @ApiOperation(value = "查看评论")
    @GetMapping("/comments")
    public Result<PageResult<CommentDTO>> listComments(CommentVO commentVO) {
        return Result.ok(commentService.listComments(commentVO));
    }

    /**
     * 查看评论下的回复
     *
     * @param commentId 评论id
     * @return 回复列表
     */
    @ApiOperation(value = "查看评论下的回复")
    @ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "Integer")
    @GetMapping("/comments/{commentId}/replies")
    public Result<List<ReplyDTO>> listRepliesByCommentId(@PathVariable("commentId") Integer commentId) {
        return Result.ok(commentService.listRepliesByCommentId(commentId));
    }

    /**
     * 添加评论
     *
     * @param commentVO 评论对象
     */
    @ApiOperation(value = "添加评论")
    @GetMapping("/comments")
    public Result<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return Result.ok();
    }

    /**
     * 删除评论
     *
     * @param commentIdList 评论id列表
     * @return 操作结果
     */
    @ApiOperation(value = "删除评论")
    @PostMapping("/admin/comments")
    public Result<?> deleteComments(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.ok();
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     */
    @ApiOperation(value = "点赞评论")
    @GetMapping("/comments/{commentId}/like")
    public Result<?> saveCommentLike(@PathVariable("commentId") Integer commentId) {
        commentService.saveCommentLike(commentId);
        return Result.ok();
    }

    /**
     * 审核评论
     *
     * @param reviewVO 审核对象
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comments/review")
    public Result<?> updateCommentsReview(@Valid @RequestBody ReviewVO reviewVO) {
        commentService.updateCommentsReview(reviewVO);
        return Result.ok();
    }

    /**
     * 查询后台评论
     *
     * @param conditionVO 条件
     * @return 评论列表
     */
    @ApiOperation(value = "查询后台评论")
    @PostMapping("/admin/comments/")
    public Result<PageResult<CommentBackDTO>> listCommentBackDTO(ConditionVO conditionVO) {
        return Result.ok(commentService.listCommentBackDTO(conditionVO));
    }
}
