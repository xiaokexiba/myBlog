package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.entity.Type;
import com.yeffxyz.blog.entity.User;
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
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
    @Resource
    private TypeMapper typeMapper;

    /**
     * 新增分类的类别
     *
     * @param type 想要保存的类别
     * @return 修改的条数
     */
    @Override
    public int saveType(Type type) {
        return typeMapper.insert(type);
    }

    /**
     * 根据id查询分类
     *
     * @param id 类别的id
     * @return id所属的类别
     */
    @Override
    public Type getType(Long id) {
        Type type = typeMapper.selectById(id);
        return type;
    }

    /**
     * 查询所有分类
     *
     * @return 所有类别
     */
    @Override
    public List<Type> getAllType() {
        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("name");
        List<Type> list = typeMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 根据分类名称查询分类
     *
     * @param name 类别名称
     * @return 该名称的类别对象
     */
    @Override
    public Type getTypeByName(String name) {
        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Type type = typeMapper.selectOne(queryWrapper);
        return type;
    }

    /**
     * 编辑修改分类
     *
     * @param type 想要修改的类别对象
     * @return 修改的条数
     */
    @Override
    public int updateType(Type type) {
        UpdateWrapper<Type> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", type.getId());
        Type update = new Type();
        update.setName(type.getName());
        return typeMapper.update(type, updateWrapper);
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
