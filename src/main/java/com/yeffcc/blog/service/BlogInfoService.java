package com.yeffcc.blog.service;

import com.yeffcc.blog.dto.BlogHomeInfoDTO;
import com.yeffcc.blog.dto.BlogBackInfoDTO;
import com.yeffcc.blog.vo.BlogInfoVO;
import com.yeffcc.blog.vo.WebsiteConfigVO;

/**
 * 博客信息业务层接口
 *
 * @author xoke
 * @date 2022/8/31
 */
public interface BlogInfoService {

    /**
     * 获取博客首页信息
     *
     * @return 博客首页信息
     */
    BlogHomeInfoDTO getBlogHomeInfo();

    /**
     * 获取后台首页信息
     *
     * @return 后台首页信息
     */
    BlogBackInfoDTO getBlogBackInfo();

    /**
     * 保存或更新网站配置
     *
     * @param websiteConfigVO 网站配置
     */
    void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

    /**
     * 获取网站配置
     *
     * @return 网站配置
     */
    WebsiteConfigVO getWebsiteConfig();

    /**
     * 获取关于我内容
     *
     * @return 关于我内容
     */
    String getAbout();

    /**
     * 修改关于我内容
     *
     * @param blogInfoVO 博客信息
     */
    void updateAbout(BlogInfoVO blogInfoVO);

    /**
     * 上传访客信息
     */
    void report();
}
