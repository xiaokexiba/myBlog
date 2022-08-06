package com.yeffxyz.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.entity.Category;

import java.util.List;

/**
 * 分类业务层接口
 *
 * @author xoke
 * @date 2022/7/28
 */
public interface TypeService extends IService<Category> {

    /**
     * 新增分类的类别
     *
     * @param category 想要保存的类别
     * @return 修改的条数
     */
    int saveType(Category category);

    /**
     * 根据id查询分类
     *
     * @param id 类别的id
     * @return id所属的类别
     */
    Category getType(Long id);

    /**
     * 查询所有分类
     *
     * @return 所有类别
     */
    List<Category> getAllType();

    /**
     * 根据分类名称查询分类
     *
     * @param name 类别名称
     * @return 该名称的类别对象
     */
    Category getTypeByName(String name);

    /**
     * 编辑修改分类
     *
     * @param category 想要修改的类别对象
     * @return 修改的条数
     */
    int updateType(Category category);

    /**
     * 删除分类
     *
     * @param id 想要删除的类别id
     */
    void deleteType(Long id);

}
