package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.dto.TagBackDTO;
import com.yeffxyz.blog.dto.TagDTO;
import com.yeffxyz.blog.entity.Tag;
import com.yeffxyz.blog.mapper.ArticleTagMapper;
import com.yeffxyz.blog.service.TagService;
import com.yeffxyz.blog.mapper.TagMapper;
import com.yeffxyz.blog.vo.ConditionVO;
import com.yeffxyz.blog.vo.PageResult;
import com.yeffxyz.blog.vo.TagVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/22
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;
    @Resource
    private ArticleTagMapper articleTagMapper;

    /**
     * 查询标签列表
     *
     * @return 标签列表
     */
    @Override
    public PageResult<TagDTO> listTags() {
        return null;
    }

    /**
     * 查询后台标签
     *
     * @param condition 条件
     * @return {@link PageResult<TagBackDTO>} 标签列表
     */
    @Override
    public PageResult<TagBackDTO> listTagBackDTO(ConditionVO condition) {
        return null;
    }

    /**
     * 搜索文章标签
     *
     * @param condition 条件
     * @return {@link List <TagDTO>} 标签列表
     */
    @Override
    public List<TagDTO> listTagsBySearch(ConditionVO condition) {
        return null;
    }

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
    @Override
    public void deleteTag(List<Integer> tagIdList) {

    }

    /**
     * 保存或更新标签
     *
     * @param tagVO 标签
     */
    @Override
    public void saveOrUpdateTag(TagVO tagVO) {

    }
}




