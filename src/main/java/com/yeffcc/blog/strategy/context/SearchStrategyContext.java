package com.yeffcc.blog.strategy.context;

import com.yeffcc.blog.dto.ArticleSearchDTO;
import com.yeffcc.blog.strategy.SearchStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yeffcc.blog.enums.SearchModeEnum.getStrategy;

/**
 * 搜索策略上下文
 *
 * @author xoke
 * @date 2022/9/12
 */
@Service
public class SearchStrategyContext {

    /**
     * 搜索模式
     */
    @Value("${search.mode}")
    private String searchMode;

    private Map<String, SearchStrategy> searchStrategyMap = new HashMap<>();

    /**
     * 执行搜索策略
     *
     * @param keywords 关键词
     * @return 文章列表
     */
    public List<ArticleSearchDTO> executeSearchStrategy(String keywords) {
        return searchStrategyMap.get(getStrategy(searchMode)).searchArticle(keywords);
    }
}
