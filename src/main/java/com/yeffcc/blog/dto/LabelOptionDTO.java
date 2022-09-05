package com.yeffcc.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 标签选择DTO
 *
 * @author xoke
 * @date 2022/8/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabelOptionDTO {

    /**
     * 选项id
     */
    private Integer id;

    /**
     * 选项名
     */
    private String label;

    /**
     * 子选项
     */
    private List<LabelOptionDTO> children;

}
