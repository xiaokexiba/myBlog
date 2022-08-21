package com.yeffxyz.blog.service.Impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.dto.*;
import com.yeffxyz.blog.entity.Article;
import com.yeffxyz.blog.entity.Category;
import com.yeffxyz.blog.enums.FilePathEnum;
import com.yeffxyz.blog.exception.BusinessException;
import com.yeffxyz.blog.mapper.ArticleMapper;
import com.yeffxyz.blog.mapper.CategoryMapper;
import com.yeffxyz.blog.service.ArticleService;
import com.yeffxyz.blog.service.RedisService;
import com.yeffxyz.blog.util.BeanCopyUtils;
import com.yeffxyz.blog.util.CommonUtils;
import com.yeffxyz.blog.util.PageUtils;
import com.yeffxyz.blog.util.UserUtils;
import com.yeffxyz.blog.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.yeffxyz.blog.constant.CommonConst.ARTICLE_SET;
import static com.yeffxyz.blog.constant.CommonConst.FALSE;
import static com.yeffxyz.blog.constant.RedisPrefixConst.*;
import static com.yeffxyz.blog.enums.ArticleStatusEnum.PUBLIC;

/**
 * 文章业务层接口实现类
 *
 * @author xoke
 * @date 2022/8/20
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private TagService tagService;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private SearchStrategyContext searchStrategyContext;
    @Resource
    private HttpSession session;
    @Resource
    private RedisService redisService;
    @Resource
    private ArticleTagService articleTagService;
    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private UploadStrategyContext uploadStrategyContext;


    /**
     * 查询文章归档
     *
     * @return 文章归档
     */
    @Override
    public PageResult<ArchiveDTO> listArchives() {
        // 获取分页数据
        Page<Article> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        // 过滤分页数据
        Page<Article> articlePage = articleMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime)
                .orderByDesc(Article::getCreateTime)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC));
        // 转化为DTO，进行数据传递
        List<ArchiveDTO> archiveDTOList = BeanCopyUtils.copyList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageResult<>(archiveDTOList, (int) articlePage.getTotal());
    }

    /**
     * 查询后台文章
     *
     * @param conditionVO 查询条件
     * @return 文章列表
     */
    @Override
    public PageResult<ArticleBackDTO> listArticleBacks(ConditionVO conditionVO) {
        // 查询文章总数
        Integer count = articleMapper.countArticleBacks(conditionVO);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台文章
        List<ArticleBackDTO> articleBackDTOList = articleMapper.listArticleBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        // 查询文章点赞和浏览量
        Map<Object, Double> viewsCountMap = redisService.zAllScore(ARTICLE_VIEWS_COUNT);
        Map<String, Object> likeCountMap = redisService.hGetAll(ARTICLE_LIKE_COUNT);
        // 进行封装
        articleBackDTOList.forEach(item -> {
            Double viewsCount = viewsCountMap.get(item.getId());
            if (Objects.nonNull(viewsCount)) {
                item.setViewsCount(viewsCount.intValue());
            }
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
        });
        return new PageResult<>(articleBackDTOList, count);
    }

    /**
     * 查询首页文章
     *
     * @return 文章列表
     */
    @Override
    public List<ArticleHomeDTO> listArticles() {
        return articleMapper.listArticles(PageUtils.getLimitCurrent(), PageUtils.getSize());
    }

    /**
     * 根据条件查询文章列表
     *
     * @param conditionVO 查询条件
     * @return 文章列表
     */
    @Override
    public ArticlePreviewListDTO listArticleByCondition(ConditionVO conditionVO) {
        // 查询文章
        List<ArticlePreviewDTO> articlePreviewDTOList = articleMapper.listArticlesByCondition(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        // 搜索条件对应名称(标签或者分类名)
        String name;
        if (Objects.nonNull(conditionVO.getCategoryId())) {
            name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                    .select(Category::getCategoryName)
                    .eq(Category::getId, conditionVO.getCategoryId())).getCategoryName();
        } else {
            name = tagService.getOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getTagName)
                    .eq(Tag::getId, conditionVO.getTagId())).getTagName();
        }
        return ArticlePreviewListDTO.builder().articlePreviewDTOList(articlePreviewDTOList).name(name).build();
    }

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return 文章列表
     */
    @Override
    public ArticleVO getArticleBackById(Integer articleId) {
        // 查询文章信息
        Article article = articleMapper.selectById(articleId);
        // 查询文章分类
        Category category = categoryMapper.selectById(article.getCategoryId());
        String categoryName = null;
        if (Objects.nonNull(category)) {
            categoryName = category.getCategoryName();
        }
        // 查询文章标签
        List<String> tagNameList = tagMapper.listTagNameByArticleId(articleId);
        // 封装数据
        ArticleVO articleVO = BeanCopyUtils.copyObject(article, ArticleVO.class);
        articleVO.setCategoryName(categoryName);
        articleVO.setTagNameList(tagNameList);
        return articleVO;
    }

    /**
     * 根据id查看文章
     *
     * @param articleId 文章id
     * @return 文章列表
     */
    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        return null;
    }

    /**
     * 点赞文章
     *
     * @param articleId 文章id
     */
    @Override
    public void saveArticleLike(Integer articleId) {
        // 判断是否点赞
        String articleLikeKey = ARTICLE_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisService.sIsMember(articleLikeKey, articleId)) {
            // 点过赞则删除文章id
            redisService.sRemove(articleLikeKey, articleId);
            // 文章点赞量-1
            redisService.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            // 未点赞则增加文章id
            redisService.sAdd(articleLikeKey, articleId);
            // 文章点赞量+1
            redisService.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
    }

    /**
     * 添加或修改文章
     *
     * @param articleVO 文章信息
     */
    @Override
    public void saveOrUpdateArticle(ArticleVO articleVO) {

    }

    /**
     * 修改文章置顶
     *
     * @param articleTopVO 文章置顶信息
     */
    @Override
    public void updateArticleTop(ArticleTopVO articleTopVO) {
        // 修改文章置顶状态
        Article article = Article.builder().id(articleTopVO.getId())
                .isTop(articleTopVO.getIsTop()).build();
        articleMapper.updateById(article);

    }

    /**
     * 删除或恢复文章
     *
     * @param deleteVO 逻辑删除对象
     */
    @Override
    public void updateArticleDelete(DeleteVO deleteVO) {
        // 修改文章逻辑删除状态
        List<Article> articleList = deleteVO.getIdList().stream().map(id -> Article.builder()
                        .id(id).isTop(FALSE).isDelete(deleteVO.getIsDelete()).build())
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    /**
     * 物理删除文章
     *
     * @param articleIdList 文章id集合
     */
    @Override
    public void deleteArticles(List<Integer> articleIdList) {
        // 删除文章标签关联
        articleTagMapper.delete(new LambdaQueryWrapper<AritcleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        // 删除文章
        articleMapper.deleteBatchIds(articleIdList);
    }

    /**
     * 导出文章
     *
     * @param articleIdList 文章id列表
     * @return {@link List}<{@link String}> 文件地址
     */
    @Override
    public List<String> exportArticles(List<Integer> articleIdList) {
        // 查询文章信息
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getArticleTitle, Article::getArticleContent)
                .in(Article::getId, articleIdList));
        // 写入文件并上传
        List<String> urlList = new ArrayList<>();
        for (Article article : articleList) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(article.getArticleContent().getBytes())) {
                String url = uploadStrategyContext.executeUploadStrategy(article.getArticleTitle() + FileExtEnum.MD.getExtName(), inputStream, FilePathEnum.MD.getPath());
                urlList.add(url);
            } catch (Exception e) {
                log.error(StrUtil.format("导入文章失败,堆栈:{}", ExceptionUtil.stacktraceToString(e)));
                throw new BusinessException("导出文章失败");
            }
        }
        return urlList;
    }

    /**
     * 更新文章浏览量
     *
     * @param articleId 文章id
     */
    public void updateArticleViewsCount(Integer articleId) {
        // 判断是否第一次访问，增加浏览量
        Set<Integer> articleSet = CommonUtils.castSet(Optional.ofNullable(session.getAttribute(ARTICLE_SET)).orElseGet(HashSet::new), Integer.class);
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET, articleSet);
            // 浏览量+1
            redisService.zIncr(ARTICLE_VIEWS_COUNT, articleId, 1d);
        }
    }
}
