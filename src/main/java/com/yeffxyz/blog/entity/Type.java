package com.yeffxyz.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 博客类型实体类
 *
 * @author xoke
 * @date 2022/7/23
 */
@Data
@TableName(value = "t_type")
public class Type implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 属于同一类型的博客集合
     */
    private List<Blog> blogs = new ArrayList<>();

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
