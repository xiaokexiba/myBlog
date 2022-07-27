package com.yeffxyz.blog.entity.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登入请求体
 *
 * @author xoke
 * @date 2022/7/27
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 325053199263253932L;

    private String username;

    private String password;

    private String checkPassword;

}
