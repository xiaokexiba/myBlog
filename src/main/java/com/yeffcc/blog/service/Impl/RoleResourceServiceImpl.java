package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.entity.RoleResource;
import com.yeffcc.blog.mapper.RoleResourceMapper;
import com.yeffcc.blog.service.RoleResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 角色资源业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/11
 */
@Slf4j
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {
}
