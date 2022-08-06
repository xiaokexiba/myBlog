package com.yeffxyz.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户VO
 *
 * @author xoke
 * @date 2022/8/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户信息对象")
public class UserVO {

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(name = "username", value = "用户名", required = true, dataType = "String")
    private String email;

    /**
     * 用户名
     */
    @Size(min = 4, message = "用户名不能少于4位")
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(name = "username", value = "用户名", required = true, dataType = "String")
    private String username;
    /**
     * 密码
     */
    @Size(min = 8, message = "密码不能少于8位")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name = "password", value = "密码", required = true, dataType = "String")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(name = "code", value = "邮箱验证码", required = true, dataType = "String")
    private String code;

}
