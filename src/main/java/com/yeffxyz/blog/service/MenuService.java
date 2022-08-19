package com.yeffxyz.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffxyz.blog.dto.LabelOptionDTO;
import com.yeffxyz.blog.dto.MenuDTO;
import com.yeffxyz.blog.dto.UserMenuDTO;
import com.yeffxyz.blog.entity.Menu;
import com.yeffxyz.blog.vo.ConditionVO;
import com.yeffxyz.blog.vo.MenuVO;

import java.util.List;

/**
 * 菜单业务层接口
 *
 * @author xoke
 * @date 2022/8/16
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查看菜单列表
     *
     * @param conditionVO 条件
     * @return 菜单列表
     */
    List<MenuDTO> listMenus(ConditionVO conditionVO);

    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单信息
     */
    void saveOrUpdateMenu(MenuVO menuVO);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    void deleteMenu(Integer menuId);

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项
     */
    List<LabelOptionDTO> listMenuOptions();

    /**
     * 查看用户菜单
     *
     * @return 菜单列表
     */
    List<UserMenuDTO> listUserMenus();

}
