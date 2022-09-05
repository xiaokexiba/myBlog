package com.yeffcc.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类选项
 *
 * @author xoke
 * @date 2022/8/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryOptionDTO {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

}
