package com.yeffcc.blog.controller;

import com.yeffcc.blog.dto.TalkBackDTO;
import com.yeffcc.blog.dto.TalkDTO;
import com.yeffcc.blog.enums.FilePathEnum;
import com.yeffcc.blog.service.TalkService;
import com.yeffcc.blog.strategy.context.UploadStrategyContext;
import com.yeffcc.blog.vo.ConditionVO;
import com.yeffcc.blog.vo.PageResult;
import com.yeffcc.blog.vo.Result;
import com.yeffcc.blog.vo.TalkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 说说控制器
 *
 * @author xoke
 * @date 2022/9/11
 */
@Api(tags = "说说模块")
@RestController
public class TalkController {

    @Resource
    private TalkService talkService;
    @Resource
    private UploadStrategyContext uploadStrategyContext;

    /**
     * 获取首页说说列表
     *
     * @return 说说列表
     */
    @ApiOperation(value = "获取首页说说列表")
    @GetMapping("/home/talks")
    public Result<List<String>> listHomeTalks() {
        return Result.ok(talkService.listHomeTalks());
    }

    /**
     * 获取说说列表
     *
     * @return 说说列表
     */
    @ApiOperation(value = "获取说说列表")
    @GetMapping("/talks")
    public Result<PageResult<TalkDTO>> listTalks() {
        return Result.ok(talkService.listTalks());
    }

    /**
     * 根据id查看说说
     *
     * @param talkId 说说id
     * @return id所属说说
     */
    @ApiOperation(value = "根据id查看说说")
    @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataType = "Integer")
    @GetMapping("/talks/{talkId}")
    public Result<TalkDTO> getTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.ok(talkService.getTalkById(talkId));
    }

    /**
     * 点赞说说
     *
     * @param talkId 说说id
     */
    @ApiOperation(value = "点赞说说")
    @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataType = "Integer")
    @GetMapping("/talks/{talkId}/like")
    public Result<?> saveTalkLike(@PathVariable("talkId") Integer talkId) {
        talkService.saveTalkLike(talkId);
        return Result.ok();
    }

    /**
     * 删除说说
     *
     * @param talkIdList 说说id列表
     */
    @ApiOperation(value = "删除说说")
    @DeleteMapping("/admin/talks")
    public Result<?> deleteTalks(@RequestBody List<Integer> talkIdList) {
        talkService.deleteTalks(talkIdList);
        return Result.ok();
    }

    /**
     * 保存或修改说说
     *
     * @param talkVO 说说信息
     */
    @ApiOperation(value = "保存或修改说说")
    @PostMapping("/admin/talks")
    public Result<?> saveOrUpdateTalk(@Valid @RequestBody TalkVO talkVO) {
        talkService.saveOrUpdateTalk(talkVO);
        return Result.ok();
    }

    /**
     * 查看后台说说列表
     *
     * @param conditionVO 查询条件
     * @return 说说列表
     */
    @ApiOperation(value = "查看后台说说列表")
    @GetMapping("/admin/talks")
    public Result<PageResult<TalkBackDTO>> listBackTalks(ConditionVO conditionVO) {
        return Result.ok(talkService.listBackTalks(conditionVO));
    }

    /**
     * 根据说说id查看后台说说
     *
     * @param talkId 说说id
     * @return 说说信息
     */
    @ApiOperation(value = "根据说说id查看后台说说")
    @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataType = "Integer")
    @PostMapping("/admin/talks/{talkId}")
    public Result<TalkBackDTO> getBackTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.ok(talkService.getBackTalkById(talkId));
    }

    /**
     * 上传说说图片
     *
     * @param file 文件
     * @return 说说图片地址
     */
    @ApiOperation(value = "上传说说图片")
    @ApiImplicitParam(name = "file", value = "说说图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/talks/images")
    public Result<String> saveTalkImages(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.TALK.getPath()));
    }
}
