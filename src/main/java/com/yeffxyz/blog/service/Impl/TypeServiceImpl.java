package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.entity.Category;
import com.yeffxyz.blog.mapper.TypeMapper;
import com.yeffxyz.blog.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类业务层接口实现类
 *
 * @author xoke
 * @date 2022/7/28
 */
@Service
@Slf4j
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Category> implements TypeService {
    @Resource
    private TypeMapper typeMapper;

    /**
     * 新增分类的类别
     *
     * @param category 想要保存的类别
     * @return 修改的条数
     */
    @Override
    public int saveType(Category category) {
        return typeMapper.insert(category);
    }

    /**
     * 根据id查询分类
     *
     * @param id 类别的id
     * @return id所属的类别
     */
    @Override
    public Category getType(Long id) {
        Category category = typeMapper.selectById(id);
        return category;
    }

    /**
     * 查询所有分类
     *
     * @return 所有类别
     */
    @Override
    public List<Category> getAllType() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("name");
        List<Category> list = typeMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 根据分类名称查询分类
     *
     * @param name 类别名称
     * @return 该名称的类别对象
     */
    @Override
    public Category getTypeByName(String name) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Category category = typeMapper.selectOne(queryWrapper);
        return category;
    }

    /**
     * 编辑修改分类
     *
     * @param category 想要修改的类别对象
     * @return 修改的条数
     */
    @Override
    public int updateType(Category category) {
        UpdateWrapper<Category> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", category.getId());
        Category update = new Category();
        update.setName(category.getName());
        return typeMapper.update(category, updateWrapper);
    }

    /**
     * 删除分类
     *
     * @param id 想要删除的类别id
     */
    @Override
    public void deleteType(Long id) {
        typeMapper.deleteById(id);
    }

}
