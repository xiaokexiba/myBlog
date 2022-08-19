package com.yeffxyz.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.dto.CategoryBackDTO;
import com.yeffxyz.blog.dto.CategoryDTO;
import com.yeffxyz.blog.dto.CategoryOptionDTO;
import com.yeffxyz.blog.entity.Category;
import com.yeffxyz.blog.vo.CategoryVO;
import com.yeffxyz.blog.vo.ConditionVO;
import com.yeffxyz.blog.vo.PageResult;

import java.util.List;

/**
 * 分类业务层接口
 *
 * @author xoke
 * @date 2022/8/19
 */
public interface CategoryService extends IService<Category> {

    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    PageResult<CategoryDTO> listCategories();

    /**
     * 查询后台分类
     *
     * @param conditionVO 条件
     * @return {@link PageResult<CategoryBackDTO>} 后台分类
     */
    PageResult<CategoryBackDTO> listBackCategories(ConditionVO conditionVO);

    /**
     * 搜索文章分类
     *
     * @param conditionVO 条件
     * @return {@link List<CategoryOptionDTO>} 分类列表
     */
    List<CategoryOptionDTO> listCategoriesBySearch(ConditionVO conditionVO);

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     */
    void deleteCategory(List<Integer> categoryIdList);

    /**
     * 添加或修改分类
     *
     * @param categoryVO 分类
     */
    void saveOrUpdateCategory(CategoryVO categoryVO);
}
