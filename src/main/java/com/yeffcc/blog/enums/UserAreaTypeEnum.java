package com.yeffcc.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户区域类型枚举
 *
 * @author xoke
 * @date 2022/9/10
 */
@Getter
@AllArgsConstructor
public enum UserAreaTypeEnum {
    /**
     * 用户
     */
    USER(1, "用户"),
    /**
     * 游客
     */
    VISITOR(2, "游客");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 获取用户区域类型
     *
     * @param type 类型
     * @return 用户区域类型枚举
     */
    public static UserAreaTypeEnum getUserAreaType(Integer type) {
        for (UserAreaTypeEnum value : UserAreaTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
