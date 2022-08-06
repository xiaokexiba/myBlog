package com.yeffxyz.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeffxyz.blog.common.BaseResponse;
import com.yeffxyz.blog.common.ResultCode;
import com.yeffxyz.blog.common.ResultUtils;
import com.yeffxyz.blog.entity.Category;
import com.yeffxyz.blog.service.TypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 分类控制器
 *
 * @author xoke
 * @date 2022/7/28
 */
@RestController
@RequestMapping("/admin")
public class TypeController {
    @Resource
    private TypeService typeService;

    /**
     * 分类查询类别列表
     *
     * @param pageNumber 所处页数
     * @param pageSize   每页条数
     * @return 分页结果
     */
    @GetMapping("/types/{pageNumber}/{pageSize}")
    //limit (pathNumber - 1) * pageSize,pageSize
    //第一页，每页五条数据
    //select * from hospital limit 0,5
    //公共方法，返回值是result类型的json数据，请求路径获取
    public BaseResponse<Page<Category>> types(@PathVariable Long pageNumber,
                                              @PathVariable Long pageSize) {
        //构造器
        Page<Category> p = new Page<>(pageNumber, pageSize);
        Page<Category> page = typeService.page(p);
        return ResultUtils.success(page);
    }

    /**
     * 增加分类
     *
     * @param category 类别
     * @return 分类结果
     */
    @PostMapping("/save")
    public BaseResponse addType(@RequestBody Category category) {
        if (category == null) {
            return ResultUtils.error(ResultCode.NULL_ERROR);
        }
        return typeService.save(category) ? ResultUtils.success(ResultCode.SUCCESS)
                : ResultUtils.error(ResultCode.SYSTEM_ERROR);
    }

    /**
     * 编辑修改分类
     *
     * @param category 修改类别
     * @return 修改结果
     */
    @PostMapping("/update")
    //update hospital_set set hosname = '西安医院' where id = 10;
    //接收Post请求返回json数据
    public BaseResponse<ResultCode> update(@RequestBody Category category) {
        Category t = typeService.getTypeByName(category.getName());
        if (t != null) {
            return ResultUtils.error(ResultCode.FAIL, "不能添加重复的分类");
        }
        boolean b = typeService.updateById(category);
        if (b) {
            return ResultUtils.success(ResultCode.SUCCESS);
        } else {
            return ResultUtils.error(ResultCode.FAIL);
        }
    }

    /**
     * 删除分类
     *
     * @param id 类别id
     * @return 删除结果
     */
    @GetMapping("/delete")
    public BaseResponse<ResultCode> delete(Long id) {
        boolean flag = typeService.removeById(id);
        if (flag) {
            return ResultUtils.success(ResultCode.SUCCESS);
        } else {
            return ResultUtils.error(ResultCode.FAIL);
        }
    }
}
