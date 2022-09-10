package com.yeffcc.blog.service;

import com.yeffcc.blog.dto.TalkBackDTO;
import com.yeffcc.blog.dto.TalkDTO;
import com.yeffcc.blog.entity.Talk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffcc.blog.vo.ConditionVO;
import com.yeffcc.blog.vo.PageResult;
import com.yeffcc.blog.vo.TalkVO;

import java.util.List;

/**
 * 说说业务层接口
 *
 * @author xoke
 * @date 2022/9/10
 */
public interface TalkService extends IService<Talk> {

    /**
     * 获取首页说说列表
     *
     * @return 说说列表
     */
    List<String> listHomeTalks();

    /**
     * 获取说说列表
     *
     * @return 说说列表
     */
    PageResult<TalkDTO> listTalks();

    /**
     * 根据id查看说说
     *
     * @param talkId 说说id
     * @return id所属说说
     */
    TalkDTO getTalkById(Integer talkId);

    /**
     * 点赞说说
     *
     * @param talkId 说说id
     */
    void saveTalkLike(Integer talkId);

    /**
     * 删除说说
     *
     * @param talkIdList 说说id列表
     */
    void deleteTalks(List<Integer> talkIdList);

    /**
     * 保存或修改说说
     *
     * @param talkVO 说说信息
     */
    void saveOrUpdateTalk(TalkVO talkVO);

    /**
     * 查看后台说说列表
     *
     * @param conditionVO 查询条件
     * @return 说说列表
     */
    PageResult<TalkBackDTO> listBackTalks(ConditionVO conditionVO);

    /**
     * 根据说说id查看后台说说
     *
     * @param talkId 说说id
     * @return 说说信息
     */
    TalkBackDTO getBackTalkById(Integer talkId);
}
