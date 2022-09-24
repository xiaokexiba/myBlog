package com.yeffcc.blog.handler;

import com.yeffcc.blog.dto.ResourceRoleDTO;
import com.yeffcc.blog.mapper.RoleMapper;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.management.relation.RoleList;
import java.util.Collection;
import java.util.List;

/**
 * 接口拦截规则
 *
 * @author xoke
 * @date 2022/9/12
 */
@Service
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    /**
     * 资源角色列表
     */
    private static List<ResourceRoleDTO> resourceRoleDTOList;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 加载资源角色信息
     */
    @PostConstruct
    private void loadDataSource() {
        resourceRoleDTOList = roleMapper.listResourceRoles();
    }

    /**
     * 清空接口角色信息
     */
    public void clearDataSource() {
        resourceRoleDTOList = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
