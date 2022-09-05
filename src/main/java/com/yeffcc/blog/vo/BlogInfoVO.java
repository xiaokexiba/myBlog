package com.yeffcc.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客信息VO
 *
 * @author xoke
 * @date 2022/8/31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "博客信息")
public class BlogInfoVO {

    /**
     * 关于我内容
     */
    @ApiModelProperty(name = "aboutContent", value = "关于我内容", required = true, dataType = "String")
    private String aboutContent;
}
