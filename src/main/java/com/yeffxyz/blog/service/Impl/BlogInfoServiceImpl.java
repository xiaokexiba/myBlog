package com.yeffxyz.blog.service.Impl;

import com.yeffxyz.blog.dto.BlogBackInfoDTO;
import com.yeffxyz.blog.dto.BlogHomeInfoDTO;
import com.yeffxyz.blog.service.BlogInfoService;
import com.yeffxyz.blog.vo.BlogInfoVO;
import com.yeffxyz.blog.vo.WebsiteConfigVO;

/**
 * 博客信息业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/31
 */
public class BlogInfoServiceImpl implements BlogInfoService {
    /**
     * 获取博客首页信息
     *
     * @return 博客首页信息
     */
    @Override
    public BlogHomeInfoDTO getBlogHomeInfo() {
        return null;
    }

    /**
     * 获取后台首页信息
     *
     * @return 后台首页信息
     */
    @Override
    public BlogBackInfoDTO getBlogBackInfo() {
        return null;
    }

    /**
     * 保存或更新网站配置
     *
     * @param websiteConfigVO 网站配置
     */
    @Override
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {

    }

    /**
     * 获取网站配置
     *
     * @return {@link WebsiteConfigVO} 网站配置
     */
    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        return null;
    }

    /**
     * 获取关于我内容
     *
     * @return 关于我内容
     */
    @Override
    public String getAbout() {
        return null;
    }

    /**
     * 修改关于我内容
     *
     * @param blogInfoVO 博客信息
     */
    @Override
    public void updateAbout(BlogInfoVO blogInfoVO) {

    }

    /**
     * 上传访客信息
     */
    @Override
    public void report() {

    }
}
