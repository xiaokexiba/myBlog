package com.yeffcc.blog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yeffcc.blog.dto.*;
import com.yeffcc.blog.entity.WebsiteConfig;
import com.yeffcc.blog.mapper.*;
import com.yeffcc.blog.service.BlogInfoService;
import com.yeffcc.blog.service.PageService;
import com.yeffcc.blog.util.IpUtils;
import com.yeffcc.blog.entity.Article;
import com.yeffcc.blog.service.RedisService;
import com.yeffcc.blog.service.UniqueViewService;
import com.yeffcc.blog.util.BeanCopyUtils;
import com.yeffcc.blog.vo.BlogInfoVO;
import com.yeffcc.blog.vo.PageVO;
import com.yeffcc.blog.vo.WebsiteConfigVO;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static com.yeffcc.blog.constant.CommonConst.*;
import static com.yeffcc.blog.constant.RedisPrefixConst.*;
import static com.yeffcc.blog.enums.ArticleStatusEnum.PUBLIC;

/**
 * 博客信息业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/31
 */
@Slf4j
@Service
public class BlogInfoServiceImpl implements BlogInfoService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private PageService pageService;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UniqueViewService uniqueViewService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private WebsiteConfigMapper websiteConfigMapper;

    /**
     * 获取博客首页信息
     *
     * @return 博客首页信息
     */
    @Override
    public BlogHomeInfoDTO getBlogHomeInfo() {
        // 查询文章数量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE)).intValue();
        // 查询分类数量
        Integer categoryCount = categoryMapper.selectCount(null).intValue();
        // 查询标签数量
        Integer tagCount = tagMapper.selectCount(null).intValue();
        // 查询访问量
        Object count = redisService.get(BLOG_VIEWS_COUNT);
        String viewsCount = Optional.ofNullable(count).orElse(0).toString();
        // 查询网站配置
        WebsiteConfigVO websiteConfigVO = this.getWebsiteConfig();
        // 查询页面图片
        List<PageVO> pageVOList = pageService.listPages();
        // 封装数据
        return BlogHomeInfoDTO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewsCount(viewsCount)
                .websiteConfig(websiteConfigVO)
                .pageList(pageVOList)
                .build();
    }

    /**
     * 获取后台首页信息
     *
     * @return 后台首页信息
     */
    @Override
    public BlogBackInfoDTO getBlogBackInfo() {
        // 查询访问量
        Object count = redisService.get(BLOG_VIEWS_COUNT);
        Integer viewsCount = Integer.parseInt(Optional.ofNullable(count).orElse(0).toString());
        // 查询留言量
        Integer messageCount = messageMapper.selectCount(null).intValue();
        // 查询用户量
        Integer userCount = userInfoMapper.selectCount(null).intValue();
        // 查询文章数量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)).intValue();
        // 查询一周用户量
        List<UniqueViewDTO> uniqueViewDTOList = uniqueViewService.listUniqueViews();
        // 查询文章统计
        List<ArticleStatisticsDTO> articleStatisticsDTOList = articleMapper.listArticleStatistics();
        // 查询分类数据
        List<CategoryDTO> categoryDTOList = categoryMapper.listCategoryDTO();
        // 查询标签数据
        List<TagDTO> tagDTOList = BeanCopyUtils.copyList(tagMapper.selectList(null), TagDTO.class);
        // 查询redis访问量前五的文章
        Map<Object, Double> articleMap = redisService.zReverseRangeWithScore(ARTICLE_VIEWS_COUNT, 0, 4);
        BlogBackInfoDTO blogBackInfoDTO = BlogBackInfoDTO.builder()
                .articleStatisticsList(articleStatisticsDTOList)
                .tagDTOList(tagDTOList)
                .viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryDTOList(categoryDTOList)
                .uniqueViewDTOList(uniqueViewDTOList)
                .build();
        if (CollectionUtils.isNotEmpty(articleMap)) {
            // 查询文章排行
            List<ArticleRankDTO> articleRankDTOList = listArticleRank(articleMap);
            blogBackInfoDTO.setArticleRankDTOList(articleRankDTOList);
        }
        return blogBackInfoDTO;
    }

    /**
     * 保存或更新网站配置
     *
     * @param websiteConfigVO 网站配置
     */
    @Override
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        // 修改网站配置
        WebsiteConfig websiteConfig = WebsiteConfig.builder()
                .id(1)
                .config(JSON.toJSONString(websiteConfigVO))
                .build();
        websiteConfigMapper.updateById(websiteConfig);
        // 删除缓存
        redisService.del(WEBSITE_CONFIG);
    }

    /**
     * 获取网站配置
     *
     * @return 网站配置
     */
    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        WebsiteConfigVO websiteConfigVO;
        // 获取网站缓存
        Object websiteConfig = redisService.get(WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        } else {
            // 从数据库中加载
            String config = websiteConfigMapper.selectById(DEFAULT_CONFIG_ID).getConfig();
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO.class);
            redisService.set(WEBSITE_CONFIG, config);
        }
        return websiteConfigVO;
    }

    /**
     * 获取关于我内容
     *
     * @return 关于我内容
     */
    @Override
    public String getAbout() {
        Object value = redisService.get(ABOUT);
        return Objects.nonNull(value) ? value.toString() : "";
    }

    /**
     * 修改关于我内容
     *
     * @param blogInfoVO 博客信息
     */
    @Override
    public void updateAbout(BlogInfoVO blogInfoVO) {
        redisService.set(ABOUT, blogInfoVO.getAboutContent());
    }

    /**
     * 上传访客信息
     */
    @Override
    public void report() {
        // 获取ip
        String ipAddress = IpUtils.getIpAddress(request);
        // 获取访问设备信息
        UserAgent userAgent = IpUtils.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 生成用户唯一标识
        String uuid = ipAddress + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisService.sIsMember(UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtils.getIpSource(ipAddress);
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(PROVINCE, "")
                        .replaceAll(CITY, "");
                redisService.hIncr(VISITOR_AREA, ipSource, 1L);
            } else {
                redisService.hIncr(VISITOR_AREA, UNKNOWN, 1L);
            }
            // 访问量+1
            redisService.incr(BLOG_VIEWS_COUNT, 1L);
            // 保存唯一标识
            redisService.sAdd(UNIQUE_VISITOR, md5);
        }
    }

    /**
     * 查询文章排行
     *
     * @param articleMap 文章信息
     * @return 文章排行
     */
    public List<ArticleRankDTO> listArticleRank(Map<Object, Double> articleMap) {
        // 提取文章id
        List<Integer> articleIdList = new ArrayList<>(articleMap.size());
        articleMap.forEach((key, value) -> articleIdList.add((Integer) key));
        // 查询文章信息
        return articleMapper.selectList(new LambdaQueryWrapper<Article>()
                        .select(Article::getId, Article::getArticleTitle)
                        .in(Article::getId, articleIdList))
                .stream().map(article -> ArticleRankDTO.builder()
                        .articleTitle(article.getArticleTitle())
                        .viewsCount(articleMap.get(article.getId()).intValue())
                        .build())
                .sorted(Comparator.comparingInt(ArticleRankDTO::getViewsCount).reversed())
                .collect(Collectors.toList());
    }
}
