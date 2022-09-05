package com.yeffcc.blog.service;

import com.yeffcc.blog.dto.TagBackDTO;
import com.yeffcc.blog.dto.TagDTO;
import com.yeffcc.blog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffcc.blog.vo.ConditionVO;
import com.yeffcc.blog.vo.PageResult;
import com.yeffcc.blog.vo.TagVO;

import java.util.List;

/**
 * 标签业务层接口
 *
 * @author xoke
 * @date 2022/8/22
 */
public interface TagService extends IService<Tag> {

    /**
     * 查询标签列表
     *
     * @return 标签列表
     */
    PageResult<TagDTO> listTags();

    /**
     * 查询后台标签
     *
     * @param conditionVO 条件
     * @return {@link PageResult<TagBackDTO>} 标签列表
     */
    PageResult<TagBackDTO> listTagBackDTO(ConditionVO conditionVO);

    /**
     * 搜索文章标签
     *
     * @param conditionVO 条件
     * @return {@link List<TagDTO>} 标签列表
     */
    List<TagDTO> listTagsBySearch(ConditionVO conditionVO);

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
    void deleteTag(List<Integer> tagIdList);

    /**
     * 保存或更新标签
     *
     * @param tagVO 标签
     */
    void saveOrUpdateTag(TagVO tagVO);
}
