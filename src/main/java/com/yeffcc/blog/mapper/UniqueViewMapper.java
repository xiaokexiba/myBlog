package com.yeffcc.blog.mapper;

import com.yeffcc.blog.dto.UniqueViewDTO;
import com.yeffcc.blog.entity.UniqueView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 一周访问量持久层接口
 *
 * @author xoke
 * @date 2022/9/12
 */
public interface UniqueViewMapper extends BaseMapper<UniqueView> {

    /**
     * 获取7天用户量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户量
     */
    List<UniqueViewDTO> listUniqueViews(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}




