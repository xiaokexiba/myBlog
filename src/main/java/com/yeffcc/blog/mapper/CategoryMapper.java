package com.yeffcc.blog.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeffcc.blog.dto.CategoryBackDTO;
import com.yeffcc.blog.dto.CategoryDTO;
import com.yeffcc.blog.entity.Category;
import com.yeffcc.blog.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类持久层接口
 *
 * @author xoke
 * @date 2022/8/19
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询分类和对应文章数量
     *
     * @return 分类列表
     */
    List<CategoryDTO> listCategoryDTO();

    /**
     * 查询后台分类列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 分类列表
     */
    List<CategoryBackDTO> listCategoryBackDTO(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

}




