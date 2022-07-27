package com.yeffxyz.blog.entity;

import lombok.Data;

import java.util.Date;

/**
 * 访问用户实体类
 *
 * @author xoke
 * @date 2022/7/23
 */
@Data
public class User {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户类型
     * 0——普通用户
     * 1——管理员
     */
    private Integer type;
    /**
     *
     */
    private String avatar;
    /**
     * 用户创建时间
     */
    private Date createTime;
    /**
     * 用户最近更新时间
     */
    private Date updateTime;
}
