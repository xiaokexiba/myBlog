package com.yeffcc.blog.mapper;

import com.yeffcc.blog.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单持久层接口
 *
 * @author xoke
 * @date 2022/8/16
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户id查询菜单
     *
     * @param userInfoId 用户信息id
     * @return 菜单列表
     */
    List<Menu> listMenusByUserInfoId(Integer userInfoId);
}




