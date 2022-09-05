package com.yeffcc.blog.controller;

import com.yeffcc.blog.dto.LabelOptionDTO;
import com.yeffcc.blog.dto.MenuDTO;
import com.yeffcc.blog.dto.UserMenuDTO;
import com.yeffcc.blog.service.MenuService;
import com.yeffcc.blog.vo.ConditionVO;
import com.yeffcc.blog.vo.MenuVO;
import com.yeffcc.blog.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 菜单控制器
 *
 * @author xoke
 * @date 2022/8/19
 */
@Api(tags = "菜单模块")
@RestController
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 查询菜单列表
     *
     * @param conditionVO 查询条件
     * @return {@link Result<MenuDTO>} 菜单列表
     */
    @ApiOperation(value = "查询菜单列表")
    @GetMapping("/admin/menus")
    public Result<List<MenuDTO>> listMenus(ConditionVO conditionVO) {
        return Result.ok(menuService.listMenus(conditionVO));
    }

    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单
     * @return {@link Result<>} 操作结果信息
     */
    @ApiOperation(value = "新增或修改菜单")
    @PostMapping("/admin/menus")
    public Result<?> saveOrUpdateMenu(@Valid @RequestBody MenuVO menuVO) {
        menuService.saveOrUpdateMenu(menuVO);
        return Result.ok();
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return {@link Result<>} 操作结果信息
     */
    public Result<?> deleteMenu(@PathVariable("menuId") Integer menuId) {
        menuService.deleteMenu(menuId);
        return Result.ok();
    }

    /**
     * 查看角色菜单选项
     *
     * @return {@link Result<LabelOptionDTO>} 角色菜单选项
     */
    public Result<List<LabelOptionDTO>> listMenuOptions() {
        return Result.ok(menuService.listMenuOptions());
    }

    /**
     * 查看当前用户菜单
     *
     * @return {@link Result<UserMenuDTO>} 当前用户菜单
     */
    public Result<List<UserMenuDTO>> listUserMenus() {
        return Result.ok(menuService.listUserMenus());
    }
}
