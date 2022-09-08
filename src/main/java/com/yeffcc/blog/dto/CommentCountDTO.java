package com.yeffcc.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论数量DTO
 *
 * @author xoke
 * @date 2022/9/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCountDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 评论数量
     */
    private Integer commentCount;
}
