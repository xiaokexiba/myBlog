package com.yeffxyz.blog.service;

import com.yeffxyz.blog.dto.RoleDTO;
import com.yeffxyz.blog.dto.UserRoleDTO;
import com.yeffxyz.blog.vo.ConditionVO;
import com.yeffxyz.blog.vo.PageResult;
import com.yeffxyz.blog.vo.RoleVO;

import java.util.List;

/**
 * 角色业务层接口
 *
 * @author xoke
 * @date 2022/8/7
 */
public interface RoleService {

    /**
     * 获取用户角色选项
     *
     * @return 角色
     */
    List<UserRoleDTO> listUserRoles();

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件
     * @return 角色列表
     */
    PageResult<RoleDTO> listRoles(ConditionVO conditionVO);

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色
     */
    void saveOrUpdateRole(RoleVO roleVO);

    /**
     * 删除角色
     * @param roleIdList 角色id列表
     */
    void deleteRoles(List<Integer> roleIdList);
}
