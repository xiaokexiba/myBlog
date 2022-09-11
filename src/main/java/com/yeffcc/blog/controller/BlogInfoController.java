package com.yeffcc.blog.controller;

import com.yeffcc.blog.dto.BlogBackInfoDTO;
import com.yeffcc.blog.dto.BlogHomeInfoDTO;
import com.yeffcc.blog.enums.FilePathEnum;
import com.yeffcc.blog.service.BlogInfoService;
import com.yeffcc.blog.strategy.context.UploadStrategyContext;
import com.yeffcc.blog.vo.BlogInfoVO;
import com.yeffcc.blog.vo.Result;
import com.yeffcc.blog.vo.WebsiteConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 博客信息控制器
 *
 * @author xoke
 * @date 2022/9/11
 */
@Api(tags = "博客信息模块")
@RestController
public class BlogInfoController {

    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private UploadStrategyContext uploadStrategyContext;

    /**
     * 获取博客首页信息
     *
     * @return 博客首页信息
     */
    @ApiOperation(value = "获取博客首页信息")
    @GetMapping("/")
    public Result<BlogHomeInfoDTO> getBlogHomeInfo() {
        return Result.ok(blogInfoService.getBlogHomeInfo());
    }

    /**
     * 获取后台首页信息
     *
     * @return 后台首页信息
     */
    @ApiOperation(value = "获取后台首页信息")
    @PostMapping("/admin")
    public Result<BlogBackInfoDTO> getBlogBackInfo() {
        return Result.ok(blogInfoService.getBlogBackInfo());
    }

    /**
     * 保存或更新网站配置
     *
     * @param websiteConfigVO 网站配置
     */
    @ApiOperation(value = "保存或更新网站配置")
    @PostMapping("/admin/website/config")
    public Result<?> updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) {
        blogInfoService.updateWebsiteConfig(websiteConfigVO);
        return Result.ok();
    }

    /**
     * 获取网站配置
     *
     * @return 网站配置
     */
    public Result<WebsiteConfigVO> getWebsiteConfig() {
        return Result.ok(blogInfoService.getWebsiteConfig());
    }

    /**
     * 获取关于我内容
     *
     * @return 关于我内容
     */
    @ApiOperation(value = "获取关于我内容")
    @GetMapping("/about")
    public Result<String> getAbout() {
        return Result.ok(blogInfoService.getAbout());
    }

    /**
     * 修改关于我内容
     *
     * @param blogInfoVO 博客信息
     */
    @ApiOperation(value = "修改关于我内容")
    @PostMapping("/admin/about")
    public Result<?> updateAbout(@Valid @RequestBody BlogInfoVO blogInfoVO) {
        blogInfoService.updateAbout(blogInfoVO);
        return Result.ok();
    }

    /**
     * 上传访客信息
     */
    @ApiOperation(value = "上传访客信息")
    @PostMapping("/report")
    public Result<?> report() {
        blogInfoService.report();
        return Result.ok();
    }

    /**
     * 上传博客配置图片
     *
     * @param file 文件
     * @return 博客配置图片地址
     */
    @ApiOperation(value = "上传博客配置图片")
    @ApiImplicitParam(name = "file", value = "博客图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/config/images")
    public Result<String> saveBlogImages(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.getPath()));
    }
}
