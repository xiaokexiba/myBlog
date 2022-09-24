package com.yeffcc.blog.strategy.context;

import com.yeffcc.blog.strategy.UploadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.yeffcc.blog.enums.UploadModeEnum.getStrategy;

/**
 * 上传策略上下文
 *
 * @author xoke
 * @date 2022/9/11
 */
@Service
public class UploadStrategyContext {

    /**
     * 上传模式
     */
    @Value("${upload.mode}")
    private String uploadMode;

    private Map<String, UploadStrategy> uploadStrategyMap = new HashMap<>();

    /**
     * 执行上传策略
     *
     * @param file 文件
     * @param path 路径
     * @return 文件地址
     */
    public String executeUploadStrategy(MultipartFile file, String path) {
        return uploadStrategyMap.get(getStrategy(uploadMode)).uploadFile(file, path);
    }

    /**
     * 执行上传策略
     *
     * @param fileName    文件
     * @param inputStream 文件输入流
     * @param path        路径
     * @return 文件地址
     */
    public String executeUploadStrategy(String fileName, InputStream inputStream, String path) {
        return uploadStrategyMap.get(getStrategy(uploadMode)).uploadFile(fileName, inputStream, path);
    }


}
