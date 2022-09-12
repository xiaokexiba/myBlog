package com.yeffcc.blog.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件工具类
 *
 * @author xoke
 * @date 2022/9/11
 */
@Slf4j
public class FileUtils {

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    public static String getExtName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
