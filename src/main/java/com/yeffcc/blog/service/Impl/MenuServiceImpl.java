package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.constant.CommonConst;
import com.yeffcc.blog.entity.RoleMenu;
import com.yeffcc.blog.exception.BusinessException;
import com.yeffcc.blog.mapper.MenuMapper;
import com.yeffcc.blog.mapper.RoleMenuMapper;
import com.yeffcc.blog.service.MenuService;
import com.yeffcc.blog.vo.ConditionVO;
import com.yeffcc.blog.vo.MenuVO;
import com.yeffcc.blog.dto.LabelOptionDTO;
import com.yeffcc.blog.dto.MenuDTO;
import com.yeffcc.blog.dto.UserMenuDTO;
import com.yeffcc.blog.entity.Menu;
import com.yeffcc.blog.util.BeanCopyUtils;
import com.yeffcc.blog.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/16
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuMapper menuMapper;

    /**
     * 查看菜单列表
     *
     * @param conditionVO 条件
     * @return 菜单列表
     */
    @Override
    public List<MenuDTO> listMenus(ConditionVO conditionVO) {
        // 查询菜单数据
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Menu::getName, conditionVO.getKeywords()));
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);
        // 组装目录菜单数据
        List<MenuDTO> menuDTOList = catalogList.stream().map(item -> {
            MenuDTO menuDTO = BeanCopyUtils.copyObject(item, MenuDTO.class);
            // 获取目录下的菜单排序
            List<MenuDTO> list = BeanCopyUtils.copyList(childrenMap.get(item.getId()), MenuDTO.class).stream()
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
            menuDTO.setChildren(list);
            childrenMap.remove(item.getId());
            return menuDTO;
        }).sorted(Comparator.comparing(MenuDTO::getOrderNum)).collect(Collectors.toList());
        // 若还有菜单未取出则拼接
        if (CollectionUtils.isNotEmpty(childrenMap)) {
            List<Menu> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<MenuDTO> childrenDTOList = childrenList.stream()
                    .map(item -> BeanCopyUtils.copyObject(item, MenuDTO.class))
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
            menuDTOList.addAll(childrenDTOList);
        }
        return menuDTOList;
    }

    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单信息
     */
    @Override
    public void saveOrUpdateMenu(MenuVO menuVO) {
        // 将VO转化成实体类对象，进行保存和更新
        Menu menu = BeanCopyUtils.copyObject(menuVO, Menu.class);
        this.saveOrUpdate(menu);
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    @Override
    public void deleteMenu(Integer menuId) {
        Long count = roleMenuMapper.selectCount(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getMenuId, menuId));
        if (count > 0) {
            throw new BusinessException("该菜单下有角色关联，不能进行删除操作");
        }
        // 查询子菜单
        List<Integer> menuIdList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                        .select(Menu::getId)
                        .eq(Menu::getParentId, menuId))
                .stream().map(Menu::getId).collect(Collectors.toList());
        menuIdList.add(menuId);
        menuMapper.deleteBatchIds(menuIdList);
    }

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项
     */
    @Override
    public List<LabelOptionDTO> listMenuOptions() {
        // 查询菜单数据
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getName, Menu::getParentId, Menu::getOrderNum));
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);
        // 组装目录菜单数据
        return catalogList.stream().map(item -> {
            // 获取目录下的菜单排序
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Menu> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> LabelOptionDTO.builder()
                                .id(menu.getId())
                                .label(menu.getName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .label(item.getName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 查看用户菜单
     *
     * @return 菜单列表
     */
    @Override
    public List<UserMenuDTO> listUserMenus() {
        // 查询菜单列表
        List<Menu> menuList = menuMapper.listMenusByUserInfoId(UserUtils.getLoginUser().getUserInfoId());
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);
        // 转换前端菜单格式
        return convertUserMenuList(catalogList, childrenMap);
    }

    /**
     * 获取目录列表
     *
     * @param menuList 菜单列表
     * @return 目录列表
     */
    private List<Menu> listCatalog(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }

    /**
     * 获取目录下菜单列表
     *
     * @param menuList 菜单列表
     * @return 目录下的菜单列表
     */
    private Map<Integer, List<Menu>> getMenuMap(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Menu::getParentId));
    }

    /**
     * 转换用户菜单格式
     *
     * @param catalogList 目录
     * @param childrenMap 子菜单
     */
    private List<UserMenuDTO> convertUserMenuList(List<Menu> catalogList, Map<Integer, List<Menu>> childrenMap) {
        return catalogList.stream().map(item -> {
            // 获取目录
            UserMenuDTO userMenuDTO = new UserMenuDTO();
            List<UserMenuDTO> list = new ArrayList<>();
            // 获取目录下的子菜单
            List<Menu> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                // 多级菜单处理
                userMenuDTO = BeanCopyUtils.copyObject(item, UserMenuDTO.class);
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> {
                            UserMenuDTO dto = BeanCopyUtils.copyObject(menu, UserMenuDTO.class);
                            dto.setHidden(menu.getIsHidden().equals(CommonConst.TRUE));
                            return dto;
                        })
                        .collect(Collectors.toList());
            } else {
                // 一级菜单处理
                userMenuDTO.setPath(item.getPath());
                userMenuDTO.setComponent(CommonConst.COMPONENT);
                list.add(UserMenuDTO.builder()
                        .path("")
                        .name(item.getName())
                        .icon(item.getIcon())
                        .component(item.getComponent())
                        .build());
            }
            userMenuDTO.setHidden(item.getIsHidden().equals(CommonConst.TRUE));
            userMenuDTO.setChildren(list);
            return userMenuDTO;
        }).collect(Collectors.toList());
    }
}
