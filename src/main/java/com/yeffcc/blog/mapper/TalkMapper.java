package com.yeffcc.blog.mapper;

import com.yeffcc.blog.dto.TalkBackDTO;
import com.yeffcc.blog.dto.TalkDTO;
import com.yeffcc.blog.entity.Talk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeffcc.blog.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 说说持久层接口
 *
 * @author xoke
 * @date 2022/9/10
 */
public interface TalkMapper extends BaseMapper<Talk> {

    /**
     * 获取说说列表
     *
     * @param current 页码
     * @param size    大小
     * @return 说说列表
     */
    List<TalkDTO> listTalks(@Param("current") Long current, @Param("size") Long size);

    /**
     * 获取后台说说列表
     *
     * @param current     页码
     * @param size        大小
     * @param conditionVO 查询条件
     * @return 说说列表
     */
    List<TalkBackDTO> listBackTalks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO conditionVO);

    /**
     * 根据说说id查看说说
     *
     * @param talkId 说说id
     * @return 说说信息
     */
    TalkDTO getTalkById(@Param("talkId") Integer talkId);

    /**
     * 根据说说id查看后台说说
     *
     * @param talkId 说说id
     * @return 说说信息
     */
    TalkBackDTO getBackTalkById(@Param("talkId") Integer talkId);
}




