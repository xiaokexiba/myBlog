package com.yeffxyz.blog.mapper;

import com.yeffxyz.blog.dto.UserBackDTO;
import com.yeffxyz.blog.entity.UserAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeffxyz.blog.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户权限持久层接口
 *
 * @author xoke
 * @date 2022/7/23
 */
@Repository
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    /**
     * 查询后台用户列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return {@link List <UserBackDTO>} 用户列表
     */
    List<UserBackDTO> listUsers(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * 查询后台用户数量
     *
     * @param condition 条件
     * @return 用户数量
     */
    Integer countUser(@Param("condition") ConditionVO condition);

}




