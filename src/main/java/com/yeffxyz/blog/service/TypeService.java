package com.yeffxyz.blog.service;

import com.yeffxyz.blog.entity.Type;

import java.util.List;

/**
 * 分类业务层接口
 *
 * @author xoke
 * @date 2022/7/28
 */
public interface TypeService {

    /**
     * 新增分类的类别
     *
     * @param type 想要保存的类别
     * @return 修改的条数
     */
    int saveType(Type type);

    /**
     * 根据id查询分类
     *
     * @param id 类别的id
     * @return id所属的类别
     */
    Type getType(Long id);

    /**
     * 查询所有分类
     *
     * @return 所有类别
     */
    List<Type> getAllType();

    /**
     * 根据分类名称查询分类
     *
     * @param name 类别名称
     * @return 该名称的类别对象
     */
    Type getTypeByName(String name);

    /**
     * 编辑修改分类
     *
     * @param type 想要修改的类别对象
     * @return 修改的条数
     */
    int updateType(Type type);

    /**
     * 删除分类
     *
     * @param id 想要删除的类别id
     */
    void deleteType(Long id);

}
