package com.yeffcc.blog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.dto.CommentCountDTO;
import com.yeffcc.blog.dto.TalkBackDTO;
import com.yeffcc.blog.dto.TalkDTO;
import com.yeffcc.blog.entity.Talk;
import com.yeffcc.blog.exception.BusinessException;
import com.yeffcc.blog.mapper.CommentMapper;
import com.yeffcc.blog.service.RedisService;
import com.yeffcc.blog.service.TalkService;
import com.yeffcc.blog.mapper.TalkMapper;
import com.yeffcc.blog.util.*;
import com.yeffcc.blog.vo.ConditionVO;
import com.yeffcc.blog.vo.PageResult;
import com.yeffcc.blog.vo.TalkVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yeffcc.blog.constant.RedisPrefixConst.TALK_LIKE_COUNT;
import static com.yeffcc.blog.constant.RedisPrefixConst.TALK_USER_LIKE;
import static com.yeffcc.blog.enums.TalkStatusEnum.PUBLIC;

/**
 * 说说业务层接口实现类
 *
 * @author xoke
 * @date 2022/9/10
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk>
        implements TalkService {

    @Resource
    private TalkMapper talkMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private RedisService redisService;

    /**
     * 获取首页说说列表
     *
     * @return 说说列表
     */
    @Override
    public List<String> listHomeTalks() {
        // 查询最近10条说说
        return talkMapper.selectList(new LambdaQueryWrapper<Talk>()
                        .eq(Talk::getStatus, PUBLIC.getStatus())
                        .orderByDesc(Talk::getIsTop)
                        .orderByDesc(Talk::getId)
                        .last("limit 10"))
                .stream()
                .map(item -> item.getContent().length() > 200 ? HTMLUtils.deleteHMTLTag(item.getContent().substring(0, 200)) : HTMLUtils.deleteHMTLTag(item.getContent()))
                .collect(Collectors.toList());
    }

    /**
     * 获取说说列表
     *
     * @return 说说列表
     */
    @Override
    public PageResult<TalkDTO> listTalks() {
        // 查询说说数量
        Integer count = talkMapper.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, PUBLIC.getStatus())).intValue();
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkDTO> talkDTOList = talkMapper.listTalks(PageUtils.getLimitCurrent(), PageUtils.getSize());
        // 查询说说评论量
        List<Integer> talkIdList = talkDTOList.stream()
                .map(TalkDTO::getId)
                .collect(Collectors.toList());
        Map<Integer, Integer> commentCountMap = commentMapper.listCommentCountByTopicIds(talkIdList)
                .stream()
                .collect(Collectors.toMap(CommentCountDTO::getId, CommentCountDTO::getCommentCount));
        // 查询说说点赞量
        Map<String, Object> likeCountMap = redisService.hGetAll(TALK_LIKE_COUNT);
        talkDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setCommentCount(commentCountMap.get(item.getId()));
            // 转化图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkDTOList, count);
    }

    /**
     * 根据id查看说说
     *
     * @param talkId 说说id
     * @return id所属说说
     */
    @Override
    public TalkDTO getTalkById(Integer talkId) {
        // 查看说说信息
        TalkDTO talkDTO = talkMapper.getTalkById(talkId);
        if (Objects.isNull(talkDTO)) {
            throw new BusinessException("说说不存在！");
        }
        // 查询说说点赞量
        talkDTO.setLikeCount((Integer) redisService.hGet(TALK_LIKE_COUNT, talkId.toString()));
        if (Objects.nonNull(talkDTO.getImages())) {
            talkDTO.setImgList(CommonUtils.castList(JSON.parseObject(talkDTO.getImages(), List.class), String.class));
        }
        return talkDTO;
    }

    /**
     * 点赞说说
     *
     * @param talkId 说说id
     */
    @Override
    public void saveTalkLike(Integer talkId) {
        // 判断是否点赞
        String talkLikeKey = TALK_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisService.sIsMember(talkLikeKey, talkId)) {
            // 点过赞则删除说说id
            redisService.sRemove(talkLikeKey, talkId);
            // 说说点赞量-1
            redisService.hDecr(TALK_LIKE_COUNT, talkId.toString(), 1L);
        } else {
            // 未点赞则增加说说id
            redisService.sAdd(talkLikeKey, talkId);
            // 说说点赞量+1
            redisService.hIncr(TALK_LIKE_COUNT, talkId.toString(), 1L);
        }
    }

    /**
     * 删除说说
     *
     * @param talkIdList 说说id列表
     */
    @Override
    public void deleteTalks(List<Integer> talkIdList) {
        talkMapper.deleteBatchIds(talkIdList);
    }

    /**
     * 保存或修改说说
     *
     * @param talkVO 说说信息
     */
    @Override
    public void saveOrUpdateTalk(TalkVO talkVO) {
        Talk talk = BeanCopyUtils.copyObject(talkVO, Talk.class);
        talk.setUserId(UserUtils.getLoginUser().getUserInfoId());
        this.saveOrUpdate(talk);
    }

    /**
     * 查看后台说说列表
     *
     * @param conditionVO 查询条件
     * @return 说说列表
     */
    @Override
    public PageResult<TalkBackDTO> listBackTalks(ConditionVO conditionVO) {
        // 查询说说总量
        Integer count = talkMapper.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(Objects.nonNull(conditionVO.getStatus()), Talk::getStatus, conditionVO.getStatus())).intValue();
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkBackDTO> talkDTOList = talkMapper.listBackTalks(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        talkDTOList.forEach(item -> {
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkDTOList, count);
    }

    /**
     * 根据说说id查看后台说说
     *
     * @param talkId 说说id
     * @return 说说信息
     */
    @Override
    public TalkBackDTO getBackTalkById(Integer talkId) {
        TalkBackDTO talkBackDTO = talkMapper.getBackTalkById(talkId);
        // 转换图片格式
        if (Objects.nonNull(talkBackDTO.getImages())) {
            talkBackDTO.setImgList(CommonUtils.castList(JSON.parseObject(talkBackDTO.getImages(), List.class), String.class));
        }
        return talkBackDTO;
    }
}




