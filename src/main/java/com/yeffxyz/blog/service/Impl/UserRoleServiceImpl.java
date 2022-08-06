package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.entity.UserRole;
import com.yeffxyz.blog.service.UserRoleService;
import com.yeffxyz.blog.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/6
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleService {

}




