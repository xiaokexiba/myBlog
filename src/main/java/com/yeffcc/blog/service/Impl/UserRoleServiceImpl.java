package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.mapper.UserRoleMapper;
import com.yeffcc.blog.entity.UserRole;
import com.yeffcc.blog.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/6
 */
@Slf4j
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleService {

}




