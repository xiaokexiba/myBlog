package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.dto.CategoryBackDTO;
import com.yeffxyz.blog.dto.CategoryDTO;
import com.yeffxyz.blog.dto.CategoryOptionDTO;
import com.yeffxyz.blog.entity.Article;
import com.yeffxyz.blog.entity.Category;
import com.yeffxyz.blog.exception.BusinessException;
import com.yeffxyz.blog.mapper.ArticleMapper;
import com.yeffxyz.blog.mapper.CategoryMapper;
import com.yeffxyz.blog.service.CategoryService;
import com.yeffxyz.blog.util.BeanCopyUtils;
import com.yeffxyz.blog.util.PageUtils;
import com.yeffxyz.blog.vo.CategoryVO;
import com.yeffxyz.blog.vo.ConditionVO;
import com.yeffxyz.blog.vo.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 分类业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/19
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    @Override
    public PageResult<CategoryDTO> listCategories() {
        return new PageResult<>(categoryMapper.listCategoryDTO(), categoryMapper.selectCount(null).intValue());
    }

    /**
     * 查询后台分类
     *
     * @param conditionVO 条件
     * @return {@link PageResult< CategoryBackDTO >} 后台分类
     */
    @Override
    public PageResult<CategoryBackDTO> listBackCategories(ConditionVO conditionVO) {
        // 查询分类数量
        Long count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Category::getCategoryName, conditionVO.getKeywords()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询分类列表
        List<CategoryBackDTO> categoryList = categoryMapper.listCategoryBackDTO(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        return new PageResult<>(categoryList, count.intValue());
    }

    /**
     * 搜索文章分类
     *
     * @param conditionVO 条件
     * @return {@link List < CategoryOptionDTO >} 分类列表
     */
    @Override
    public List<CategoryOptionDTO> listCategoriesBySearch(ConditionVO conditionVO) {
        // 搜索分类
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Category::getCategoryName, conditionVO.getKeywords()));
        return BeanCopyUtils.copyList(categoryList, CategoryOptionDTO.class);
    }

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     */
    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        // 判断该分类下是否有文章
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList));
        if (count > 0) {
            throw new BusinessException("删除失败，该分类下还存在文章");
        }
        categoryMapper.deleteBatchIds(categoryIdList);
    }

    /**
     * 添加或修改分类
     *
     * @param categoryVO 分类
     */
    @Override
    public void saveOrUpdateCategory(CategoryVO categoryVO) {
        // 判断分类名重复
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, categoryVO.getCategoryName()));
        if (Objects.nonNull(existCategory) && !existCategory.getId().equals(categoryVO.getId())) {
            throw new BusinessException("该分类名已经存在，请重新设置");
        }
        Category category = Category.builder()
                .id(categoryVO.getId())
                .categoryName(categoryVO.getCategoryName())
                .build();
        this.saveOrUpdate(category);
    }
}
