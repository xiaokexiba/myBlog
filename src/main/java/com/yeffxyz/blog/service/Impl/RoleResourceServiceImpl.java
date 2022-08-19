package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.entity.RoleResource;
import com.yeffxyz.blog.mapper.RoleResourceMapper;
import com.yeffxyz.blog.service.RoleResourceService;
import org.springframework.stereotype.Service;

/**
 * 角色资源业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/11
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {
}
