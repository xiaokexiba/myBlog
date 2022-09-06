package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.dto.CommentBackDTO;
import com.yeffcc.blog.dto.CommentDTO;
import com.yeffcc.blog.dto.ReplyDTO;
import com.yeffcc.blog.entity.Comment;
import com.yeffcc.blog.mapper.CommentMapper;
import com.yeffcc.blog.service.CommentService;
import com.yeffcc.blog.service.RedisService;
import com.yeffcc.blog.util.PageUtils;
import com.yeffcc.blog.vo.CommentVO;
import com.yeffcc.blog.vo.ConditionVO;
import com.yeffcc.blog.vo.PageResult;
import com.yeffcc.blog.vo.ReviewVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yeffcc.blog.constant.CommonConst.TRUE;
import static com.yeffcc.blog.constant.RedisPrefixConst.COMMENT_LIKE_COUNT;

/**
 * 评论业务层接口实现类
 *
 * @author xoke
 * @date 2022/9/6
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private RedisService redisService;

    /**
     * 查看评论
     *
     * @param commentVO 评论信息
     * @return 评论列表
     */
    @Override
    public PageResult<CommentDTO> listComment(CommentVO commentVO) {
        // 查询评论量
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(commentVO.getTopicId()), Comment::getTopicId, commentVO.getTopicId())
                .eq(Comment::getType, commentVO.getType())
                .isNull(Comment::getParentId)
                .eq(Comment::getIsReview, TRUE)).intValue();
        if (commentCount == 0) {
            return new PageResult<>();
        }
        // 分页查询评论数据
        List<CommentDTO> commentDTOList = commentMapper.listComments(PageUtils.getLimitCurrent(), PageUtils.getSize(), commentVO);
        if (CollectionUtils.isEmpty(commentDTOList)) {
            return new PageResult<>();
        }
        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisService.hGetAll(COMMENT_LIKE_COUNT);
        // 提取评论id集合
        List<Integer> commentIdList = commentDTOList.stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
        // 根据评论id集合查询回复数据
        List<ReplyDTO> replyDTOList = commentMapper.listReplies(commentIdList);
        // 封装回复点赞数量
        replyDTOList.forEach(item -> item.setLikeCount((Integer) likeCountMap.get(item.getId().toString())));
        // 根据评论id分组回复数据
        Map<Integer, List<ReplyDTO>> replyMap = replyDTOList.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));
        // 根据评论id查询回复量
        Map<Integer, Integer> replyCountMap = commentMapper.listReplyCountByCommentId(commentIdList)
                .stream().collect(Collectors.toMap(ReplyCountDTO::getCommentId, ReplyCountDTO::getReplyCount));
        // 封装评论数据
        commentDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setReplyDTOList(replyMap.get(item.getId()));
            item.setReplyCount(replyCountMap.get(item.getId()));
        });
        return new PageResult<>(commentDTOList, commentCount);
    }

    /**
     * 查看评论下的回复
     *
     * @param commentId 评论id
     * @return 回复列表
     */
    @Override
    public List<ReplyDTO> listReplyByCommentId(Integer commentId) {
        return null;
    }

    /**
     * 添加评论
     *
     * @param commentVO 评论对象
     */
    @Override
    public void saveComment(CommentVO commentVO) {

    }

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     */
    @Override
    public void saveCommentLike(Integer commentId) {

    }

    /**
     * 审核评论
     *
     * @param reviewVO 审核对象
     */
    @Override
    public void updateCommentsReview(ReviewVO reviewVO) {

    }

    /**
     * 查询后台评论
     *
     * @param conditionVO 条件
     * @return 评论列表
     */
    @Override
    public PageResult<CommentBackDTO> listCommentBackDTO(ConditionVO conditionVO) {
        return null;
    }
}




