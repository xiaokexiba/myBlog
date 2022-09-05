package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.dto.UniqueViewDTO;
import com.yeffcc.blog.entity.UniqueView;
import com.yeffcc.blog.mapper.UniqueViewMapper;
import com.yeffcc.blog.service.UniqueViewService;

import java.util.List;

/**
 * 用户量业务层接口实现类
 *
 * @author xoke
 * @date 2022/9/5
 */
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {
    /**
     * 查询7天用户量
     *
     * @return 7天用户量
     */
    @Override
    public List<UniqueViewDTO> listUniqueViews() {
        return null;
    }
}
