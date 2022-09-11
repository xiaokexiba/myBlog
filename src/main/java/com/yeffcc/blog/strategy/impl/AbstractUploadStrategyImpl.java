package com.yeffcc.blog.strategy.impl;

import com.yeffcc.blog.exception.BusinessException;
import com.yeffcc.blog.strategy.UploadStrategy;
import com.yeffcc.blog.util.FileUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 抽象上传模板
 *
 * @author xoke
 * @date 2022/9/11
 */
@Service
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 上传路径
     * @return 文件地址
     */
    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            // 获取文件MD5值
            String md5 = DigestUtils.md5Hex(new FileInputStream(path));
            // 获取文件扩展名
            String extName = FileUtils.getExtName(file.getName());
            // 重新生成文件名
            String fileName = md5 + extName;
            // 判断是否存在
            if (!exists(fileName)) {
                upload(path, fileName, file.getInputStream());
            }
            // 返回文件访问路径
            return getFileAccessUrl(path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("文件上传失败！");
        }
    }

    /**
     * 上传文件
     *
     * @param fileName    文件名
     * @param inputStream 输入流
     * @param path        路径
     * @return 文件地址
     */
    @Override
    public String uploadFile(String fileName, InputStream inputStream, String path) {
        try {
            // 上传文件
            upload(path, fileName, inputStream);
            // 返回文件访问路径
            return getFileAccessUrl(path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("文件上传失败");
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    public abstract Boolean exists(String filePath);

    /**
     * 上传
     *
     * @param path        路径
     * @param fileName    文件名
     * @param inputStream 输入流
     * @throws IOException io异常
     */
    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    /**
     * 获取文件访问url
     *
     * @param filePath 文件路径
     * @return 文件url
     */
    public abstract String getFileAccessUrl(String filePath);
}
