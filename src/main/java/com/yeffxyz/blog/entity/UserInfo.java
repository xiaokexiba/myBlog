package com.yeffxyz.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户信息实体类
 *
 * @author xoke
 * @date 2022/7/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_info")
public class UserInfo {

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String intro;

    /**
     * 是否禁用
     */
    private Integer isDisable;

    /**
     * 用户创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 用户最近更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
