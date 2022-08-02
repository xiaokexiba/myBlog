package com.yeffxyz.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yeffxyz.blog.common.BaseResponse;
import com.yeffxyz.blog.common.ResultCode;
import com.yeffxyz.blog.common.ResultUtils;
import com.yeffxyz.blog.entity.Blog;
import com.yeffxyz.blog.entity.Type;
import com.yeffxyz.blog.entity.User;
import com.yeffxyz.blog.exception.BusinessException;
import com.yeffxyz.blog.service.BlogService;
import com.yeffxyz.blog.service.TypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 博客控制器
 *
 * @author xoke
 * @date 2022/8/1
 */
@RestController
@RequestMapping("/admin")
public class BlogController {
    @Resource
    private BlogService blogService;
    @Resource
    private TypeService typeService;

    /**
     * 跳转博客新增页面
     *
     * @param request 前端数据
     * @return 跳转结果
     */
    @GetMapping("/blogs/input")
    public BaseResponse input(HttpServletRequest request) {
        request.setAttribute("types", typeService.getAllType());
        request.setAttribute("blog", new Blog());
        return ResultUtils.success(ResultCode.SUCCESS);
    }

    /**
     * 博客新增
     *
     * @param blog    新增博客
     * @param request 前端数据
     * @return 新增结果
     */
    @PostMapping("/blogs")
    public BaseResponse<ResultCode> addBlog(Blog blog, HttpServletRequest request) {
        //新增的时候需要传递blog对象，blog对象需要有user
        blog.setUser((User) request.getAttribute("user"));
        //设置blog的type
        blog.setType(typeService.getType(blog.getType().getId()));
        //设置blog中typeId属性
        blog.setTypeId(blog.getType().getId());
        //设置用户id
        blog.setUserId(blog.getUser().getId());

        int b = blogService.saveBlog(blog);
        if (b == 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "新增失败");
        } else {
            return ResultUtils.success(ResultCode.SUCCESS);
        }
    }

    /**
     * 博客列表
     *
     * @param request 前端数据
     * @param pageNum 页数
     * @return 操作情况
     */
    @RequestMapping("/blogs")
    public BaseResponse<ResultCode> blogs(HttpServletRequest request,
                                          @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {

        //按照排序字段 倒序 排序
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum, 10, orderBy);
        List<Blog> list = blogService.getAllBlog();
        PageInfo<Blog> pageInfo = new PageInfo<>(list);
        request.setAttribute("types", typeService.getAllType());
        request.setAttribute("pageInfo", pageInfo);
        return ResultUtils.success(ResultCode.SUCCESS);

    }

    /**
     * 删除博客
     *
     * @param id 博客id
     * @return 删除结果
     */
    @GetMapping("/blogs/{id}/delete")
    public BaseResponse<ResultCode> delete(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResultUtils.success(ResultCode.SUCCESS);
    }

    /**
     * 跳转编辑修改文章
     *
     * @param id      博客id
     * @param request 前端数据
     * @return 修改情况
     */
    @GetMapping("/blogs/{id}/input")
    public BaseResponse<ResultCode> editInput(@PathVariable Long id, HttpServletRequest request) {
        Blog blogById = blogService.getBlogById(id);
        List<Type> allType = typeService.getAllType();
        request.setAttribute("blog", blogById);
        request.setAttribute("types", allType);
        return ResultUtils.success(ResultCode.SUCCESS);
    }

    /**
     * 编辑修改文章
     *
     * @param blog 博客
     * @return 修改结果情况
     */
    @PostMapping("/blogs/{id}")
    public BaseResponse<ResultCode> editPost(@Valid Blog blog) {
        int b = blogService.updateBlog(blog);
        if (b == 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "修改失败");
        } else {
            return ResultUtils.success(ResultCode.SUCCESS);
        }
    }

    /**
     * 搜索博客管理列表
     *
     * @param blog    博客
     * @param request 前端数据
     * @param pageNum 页数
     * @return 查询结果情况
     */
    @PostMapping("/blogs/search")
    public BaseResponse<ResultCode> search(Blog blog, HttpServletRequest request,
                                           @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
        List<Blog> blogBySearch = blogService.searchByTitleAndType(blog);
        PageHelper.startPage(pageNum, 10);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogBySearch);
        request.setAttribute("pageInfo", pageInfo);
        return ResultUtils.success(ResultCode.SUCCESS);
    }

}
