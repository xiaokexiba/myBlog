package com.yeffcc.blog.strategy.context;

import com.yeffcc.blog.enums.MarkdownTypeEnum;
import com.yeffcc.blog.strategy.ArticleImportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 文章导入策略上下文
 *
 * @author xoke
 * @date 2022/9/11
 */
@Service
public class ArticleImportStrategyContext {

    private Map<String, ArticleImportStrategy> articleImportStrategyMap = new HashMap<>();

    /**
     * 导入文章
     *
     * @param file 文件
     * @param type 类型
     */
    public void importArticles(MultipartFile file, String type) {
        articleImportStrategyMap.get(MarkdownTypeEnum.getMarkdownType(type)).importArticles(file);
    }
}
