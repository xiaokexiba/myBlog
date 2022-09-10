package com.yeffcc.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 微信登入VO
 *
 * @author xoke
 * @date 2022/8/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "微信登录信息")
public class WeChatLoginVO {

    /**
     * code
     */
    @NotBlank(message = "code不能为空")
    @ApiModelProperty(name = "openId", value = "qq openId", required = true, dataType = "String")
    private String code;
}
