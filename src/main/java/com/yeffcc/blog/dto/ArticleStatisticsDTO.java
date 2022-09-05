package com.yeffcc.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章统计DTO
 *
 * @author xoke
 * @date 2022/8/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleStatisticsDTO {

    /**
     * 日期
     */
    private String date;

    /**
     * 数量
     */
    private Integer count;

}
