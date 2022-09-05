package com.yeffcc.blog.controller;

import com.yeffcc.blog.annotation.OptLog;
import com.yeffcc.blog.dto.RoleDTO;
import com.yeffcc.blog.dto.UserRoleDTO;
import com.yeffcc.blog.service.RoleService;
import com.yeffcc.blog.vo.ConditionVO;
import com.yeffcc.blog.vo.PageResult;
import com.yeffcc.blog.vo.Result;
import com.yeffcc.blog.vo.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.yeffcc.blog.constant.OptTypeConst.REMOVE;
import static com.yeffcc.blog.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * 角色控制器
 *
 * @author xoke
 * @date 2022/8/11
 */
@Api(tags = "角色模块")
@RestController
public class RoleController {
    @Resource
    private RoleService roleService;

    /**
     * 查询用户角色选项
     *
     * @return {@link Result<UserRoleDTO>} 用户角色选项
     */
    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    public Result<List<UserRoleDTO>> listUserRoles() {
        return Result.ok(roleService.listUserRoles());
    }

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件
     * @return {@link Result<RoleDTO>} 角色列表
     */
    @ApiOperation(value = "查询角色列表")
    @GetMapping("/admin/roles")
    public Result<PageResult<RoleDTO>> listRoles(ConditionVO conditionVO) {
        return Result.ok(roleService.listRoles(conditionVO));
    }

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    public Result<?> saveOrUpdateRole(@RequestBody @Valid RoleVO roleVO) {
        roleService.saveOrUpdateRole(roleVO);
        return Result.ok();
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/roles")
    public Result<?> deleteRoles(@RequestBody List<Integer> roleIdList) {
        roleService.deleteRoles(roleIdList);
        return Result.ok();
    }

}
