package com.yeffcc.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffcc.blog.dto.UniqueViewDTO;
import com.yeffcc.blog.entity.UniqueView;

import java.util.List;

/**
 * 用户量业务层接口
 *
 * @author xoke
 * @date 2022/9/5
 */
public interface UniqueViewService extends IService<UniqueView> {
    /**
     * 查询7天用户量
     *
     * @return 7天用户量
     */
    List<UniqueViewDTO> listUniqueViews();
}
