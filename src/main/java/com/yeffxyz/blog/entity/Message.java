package com.yeffxyz.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 留言实体类
 *
 * @author xoke
 * @date 2022/9/2
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_message")
public class Message {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ip
     */
    private String ipAddress;

    /**
     * 用户地址
     */
    private String ipSource;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 弹幕速度
     */
    private Integer time;

    /**
     * 是否审核
     */
    private Integer isReview;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
