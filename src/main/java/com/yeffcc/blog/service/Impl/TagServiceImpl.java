package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.dto.TagBackDTO;
import com.yeffcc.blog.entity.ArticleTag;
import com.yeffcc.blog.entity.Tag;
import com.yeffcc.blog.exception.BusinessException;
import com.yeffcc.blog.mapper.ArticleTagMapper;
import com.yeffcc.blog.mapper.TagMapper;
import com.yeffcc.blog.service.TagService;
import com.yeffcc.blog.dto.TagDTO;
import com.yeffcc.blog.util.BeanCopyUtils;
import com.yeffcc.blog.util.PageUtils;
import com.yeffcc.blog.vo.ConditionVO;
import com.yeffcc.blog.vo.PageResult;
import com.yeffcc.blog.vo.TagVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
        // 查询标签列表
        List<Tag> tagList = tagMapper.selectList(null);
        // 转换DTO
        List<TagDTO> tagDTOList = BeanCopyUtils.copyList(tagList, TagDTO.class);
        // 查询标签数量
        Integer count = tagMapper.selectCount(null).intValue();
        return new PageResult<>(tagDTOList, count);
    }

    /**
     * 查询后台标签
     *
     * @param conditionVO 条件
     * @return 标签列表
     */
    @Override
    public PageResult<TagBackDTO> listTagBackDTO(ConditionVO conditionVO) {
        // 查询标签数量
        int count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Tag::getTagName, conditionVO.getKeywords())).intValue();
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询标签列表
        List<TagBackDTO> tagList = tagMapper.listTagBackDTO(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        return new PageResult<>(tagList, count);
    }

    /**
     * 搜索文章标签
     *
     * @param conditionVO 条件
     * @return 标签列表
     */
    @Override
    public List<TagDTO> listTagsBySearch(ConditionVO conditionVO) {
        // 搜索标签
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Tag::getTagName, conditionVO.getKeywords())
                .orderByDesc(Tag::getId));
        return BeanCopyUtils.copyList(tagList, TagDTO.class);
    }

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
    @Override
    public void deleteTag(List<Integer> tagIdList) {
        // 查询该标签下是否有文章
        int count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getId, tagIdList)).intValue();
        if (count > 0) {
            throw new BusinessException("该标签下存在文章，不能进行删除");
        }
        tagMapper.deleteBatchIds(tagIdList);
    }

    /**
     * 保存或更新标签
     *
     * @param tagVO 标签
     */
    @Override
    public void saveOrUpdateTag(TagVO tagVO) {
        // 查询标签名是否存在
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName, tagVO.getTagName()));
        if (Objects.nonNull(existTag) && !existTag.getId().equals(tagVO.getId())) {
            throw new BusinessException("该标签已存在！");
        }
        Tag tag = BeanCopyUtils.copyObject(tagVO, Tag.class);
        this.saveOrUpdate(tag);
    }
}




