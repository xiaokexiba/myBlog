package com.yeffcc.blog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.dto.*;
import com.yeffcc.blog.entity.Comment;
import com.yeffcc.blog.mapper.ArticleMapper;
import com.yeffcc.blog.mapper.CommentMapper;
import com.yeffcc.blog.mapper.TalkMapper;
import com.yeffcc.blog.mapper.UserInfoMapper;
import com.yeffcc.blog.service.BlogInfoService;
import com.yeffcc.blog.service.CommentService;
import com.yeffcc.blog.service.RedisService;
import com.yeffcc.blog.util.HTMLUtils;
import com.yeffcc.blog.util.PageUtils;
import com.yeffcc.blog.util.UserUtils;
import com.yeffcc.blog.vo.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.yeffcc.blog.constant.CommonConst.*;
import static com.yeffcc.blog.constant.MQPrefixConst.EMAIL_EXCHANGE;
import static com.yeffcc.blog.constant.RedisPrefixConst.COMMENT_LIKE_COUNT;
import static com.yeffcc.blog.constant.RedisPrefixConst.COMMENT_USER_LIKE;
import static com.yeffcc.blog.enums.CommentTypeEnum.getCommentEnum;
import static com.yeffcc.blog.enums.CommentTypeEnum.getCommentPath;

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
    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TalkMapper talkMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 网站网址
     */
    @Value("${website.url}")
    private String websiteUrl;

    /**
     * 查看评论
     *
     * @param commentVO 评论信息
     * @return 评论列表
     */
    @Override
    public PageResult<CommentDTO> listComments(CommentVO commentVO) {
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
    public List<ReplyDTO> listRepliesByCommentId(Integer commentId) {
        // 转化页码查询评论下的回复
        List<ReplyDTO> replyDTOList = commentMapper.listRepliesByCommentId(PageUtils.getLimitCurrent(), PageUtils.getSize(), commentId);
        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisService.hGetAll(COMMENT_LIKE_COUNT);
        // 封装点赞数据
        replyDTOList.forEach(item -> item.setLikeCount((Integer) likeCountMap.get(item.getId().toString())));
        return replyDTOList;
    }

    /**
     * 添加评论
     *
     * @param commentVO 评论对象
     */
    @Override
    public void saveComment(CommentVO commentVO) {
        // 判断是否需要审核
        WebsiteConfigVO websiteConfigVO = blogInfoService.getWebsiteConfig();
        Integer isReview = websiteConfigVO.getIsCommentReview();
        // 过滤标签
        commentVO.setCommentContent(HTMLUtils.filter(commentVO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtils.getLoginUser().getUserInfoId())
                .replyUserId(commentVO.getReplyUserId())
                .topicId(commentVO.getTopicId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .type(commentVO.getType())
                .isReview(isReview == TRUE ? TRUE : FALSE)
                .build();
        commentMapper.insert(comment);
        // 判断是否开启邮箱通知，通知用户
        if (websiteConfigVO.getIsEmailNotice().equals(TRUE)) {
            CompletableFuture.runAsync(() -> notice(comment));
        }
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     */
    @Override
    public void saveCommentLike(Integer commentId) {
        // 判断是否点赞
        String commentLikeKey = COMMENT_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisService.sIsMember(commentLikeKey, commentId)) {
            // 点过赞则删除评论id
            redisService.sRemove(commentLikeKey, commentId);
            // 评论点赞量-1
            redisService.hDecr(COMMENT_LIKE_COUNT, commentId.toString(), 1L);
        } else {
            // 未点赞则增加评论id
            redisService.sAdd(commentLikeKey, commentId);
            // 评论点赞量+1
            redisService.hIncr(COMMENT_LIKE_COUNT, commentId.toString(), 1L);
        }
    }

    /**
     * 审核评论
     *
     * @param reviewVO 审核对象
     */
    @Override
    public void updateCommentsReview(ReviewVO reviewVO) {
        // 修改评论审核状态
        List<Comment> commentList = reviewVO.getIdList().stream().map(item -> Comment.builder()
                        .id(item)
                        .isReview(reviewVO.getIsReview())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    /**
     * 查询后台评论
     *
     * @param conditionVO 条件
     * @return 评论列表
     */
    @Override
    public PageResult<CommentBackDTO> listCommentBackDTO(ConditionVO conditionVO) {
        // 统计后台评论量
        Integer count = commentMapper.countCommentDTO(conditionVO);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台评论集合
        List<CommentBackDTO> commentBackDTOList = commentMapper.listCommentBackDTO(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        return new PageResult<>(commentBackDTOList, count);
    }

    /**
     * 通知评论用户
     *
     * @param comment 评论信息
     */
    public void notice(Comment comment) {
        // 查询回复用户邮箱号
        Integer userId = BLOGGER_ID;
        String id = Objects.nonNull(comment.getTopicId()) ? comment.getTopicId().toString() : "";
        if (Objects.nonNull(comment.getReplyUserId())) {
            userId = comment.getReplyUserId();
        } else {
            switch (Objects.requireNonNull(getCommentEnum(comment.getType()))) {
                case ARTICLE:
                    userId = articleMapper.selectById(comment.getTopicId()).getUserId();
                    break;
                case TALK:
                    userId = talkMapper.selectById(comment.getTopicId()).getUserId();
                    break;
                default:
                    break;
            }
            String email = userInfoMapper.selectById(userId).getEmail();
            if (StringUtils.isNotBlank(email)) {
                // 发送消息
                EmailDTO emailDTO = new EmailDTO();
                if (comment.getIsReview().equals(TRUE)) {
                    // 评论提醒
                    emailDTO.setEmail(email);
                    emailDTO.setSubject("评论提醒");
                    // 获取评论路径
                    String url = websiteUrl + getCommentPath(comment.getType()) + id;
                    emailDTO.setContent("您收到了一条新的回复，请前往" + url + "\n页面进行查看！");
                } else {
                    // 管理员审核提醒
                    String adminEmail = userInfoMapper.selectById(BLOGGER_ID).getEmail();
                    emailDTO.setEmail(email);
                    emailDTO.setSubject("审核提醒");
                    String url = websiteUrl + getCommentPath(comment.getType()) + id;
                    emailDTO.setContent("您收到了一条新的回复，请自行前往后台管理页面审核！");
                }
                rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
            }
        }
    }
}




