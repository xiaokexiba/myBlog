package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.entity.RoleMenu;
import com.yeffcc.blog.mapper.RoleMenuMapper;
import com.yeffcc.blog.service.RoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户菜单业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/11
 */
@Slf4j
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
