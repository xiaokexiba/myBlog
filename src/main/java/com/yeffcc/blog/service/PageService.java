package com.yeffcc.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeffcc.blog.entity.Page;
import com.yeffcc.blog.vo.PageVO;

import java.util.List;

/**
 * 页面业务层接口
 *
 * @author xoke
 * @date 2022/9/2
 */
public interface PageService extends IService<Page> {

    /**
     * 保存或更新页面
     *
     * @param pageVO 页面信息
     */
    void saveOrUpdatePage(PageVO pageVO);

    /**
     * 删除页面
     *
     * @param pageId 页面id
     */
    void deletePage(Integer pageId);

    /**
     * 获取页面列表
     *
     * @return 页面列表
     */
    List<PageVO> listPages();
}
