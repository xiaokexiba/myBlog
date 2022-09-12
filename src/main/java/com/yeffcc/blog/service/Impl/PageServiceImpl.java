package com.yeffcc.blog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.entity.Page;
import com.yeffcc.blog.mapper.PageMapper;
import com.yeffcc.blog.service.PageService;
import com.yeffcc.blog.service.RedisService;
import com.yeffcc.blog.util.BeanCopyUtils;
import com.yeffcc.blog.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.yeffcc.blog.constant.RedisPrefixConst.PAGE_COVER;

/**
 * 页面业务层接口实现类
 *
 * @author xoke
 * @date 2022/9/12
 */
@Slf4j
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    @Resource
    private PageMapper pageMapper;
    @Resource
    private RedisService redisService;

    /**
     * 保存或更新页面
     *
     * @param pageVO 页面信息
     */
    @Override
    public void saveOrUpdatePage(PageVO pageVO) {
        Page page = BeanCopyUtils.copyObject(pageVO, Page.class);
        this.saveOrUpdate(page);
        // 删除缓存
        redisService.del(PAGE_COVER);
    }

    /**
     * 删除页面
     *
     * @param pageId 页面id
     */
    @Override
    public void deletePage(Integer pageId) {
        pageMapper.deleteById(pageId);
        // 删除缓存
        redisService.del(PAGE_COVER);
    }

    /**
     * 获取页面列表
     *
     * @return 页面列表
     */
    @Override
    public List<PageVO> listPages() {
        List<PageVO> pageVOList;
        // 查找缓存信息，不存在则从mysql读取，更新缓存
        Object pageList = redisService.get(PAGE_COVER);
        if (Objects.nonNull(pageList)) {
            pageVOList = JSON.parseObject(pageList.toString(), List.class);
        } else {
            pageVOList = BeanCopyUtils.copyList(pageMapper.selectList(null), PageVO.class);
            redisService.set(PAGE_COVER, JSON.toJSONString(pageVOList));
        }
        return pageVOList;
    }
}
