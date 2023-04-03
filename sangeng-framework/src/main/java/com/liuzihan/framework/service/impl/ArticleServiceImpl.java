package com.liuzihan.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuzihan.framework.constants.SystemConstants;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.Category;
import com.liuzihan.framework.domain.vo.ArticleDetailVo;
import com.liuzihan.framework.domain.vo.ArticleListVo;
import com.liuzihan.framework.domain.vo.HotArticleVo;
import com.liuzihan.framework.domain.vo.PageVo;
import com.liuzihan.framework.mapper.ArticleMapper;
import com.liuzihan.framework.domain.entity.Article;
import com.liuzihan.framework.service.ArticleService;
import com.liuzihan.framework.service.CategoryService;
import com.liuzihan.framework.utils.BeanCopyUtils;
import com.liuzihan.framework.utils.RedisCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author liuzihan
 * @since 2022-03-31 11:29:40
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询4条
        Page<Article> page = new Page(1, 4);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
        //bean拷贝
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult topArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.eq(Article::getIsTop, "1");
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getCreateTime);
        List<Article> articles = list(queryWrapper);
        List<Article> collect = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //bean拷贝
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(collect, ArticleListVo.class);

        return ResponseResult.okResult(articleListVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId,String level) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(categoryId) && Objects.nonNull(level)){
            // 如果 有categoryId 就要 查询时要和传入的相同
            List ids = categoryService.getChildrenIds(categoryId,level,false);
            lambdaQueryWrapper.in(Article::getCategoryId,ids);
        }
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        lambdaQueryWrapper.eq(Article::getIsTop, "0");
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getCreateTime);

        PageVo pageVo = getPageVo(pageNum, pageSize, lambdaQueryWrapper);
        return ResponseResult.okResult(pageVo);
    }

    @NotNull
    private PageVo getPageVo(Integer pageNum, Integer pageSize, LambdaQueryWrapper<Article> lambdaQueryWrapper) {
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        //查询categoryName
        List<Article> collect = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                // 按isTop排序
                .sorted((o1, o2) -> (Integer.parseInt(o2.getIsTop()) - Integer.parseInt(o1.getIsTop())))
                .collect(Collectors.toList());
        //articleId去查询articleName进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(collect, ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return pageVo;
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.REDIS_ARTICLE_VIEW_COUNT_KEY, id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.REDIS_ARTICLE_VIEW_COUNT_KEY, id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectArticle(Integer pageNum, Integer pageSize, Article article,String level) {
        // 按条件查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(article)) {
            queryWrapper.eq(Objects.nonNull(article.getId()),Article::getId,article.getId());
            queryWrapper.eq(Objects.nonNull(article.getStatus()), Article::getStatus, article.getStatus());
            queryWrapper.eq(Objects.nonNull(article.getIsComment()), Article::getIsComment, article.getIsComment());
            queryWrapper.eq(Objects.nonNull(article.getIsTop()), Article::getIsTop, article.getIsTop());
            if (Objects.nonNull(article.getCategoryId()) && article.getCategoryId() > 0 && Objects.nonNull(level)){
                List<Long> ids = categoryService.getChildrenIds(article.getCategoryId(), level,true);
                queryWrapper.in( !ids.isEmpty(), Article::getCategoryId, ids);
            }
            queryWrapper.like(StringUtils.hasText(article.getTitle()), Article::getTitle, article.getTitle());
            queryWrapper.like(StringUtils.hasText(article.getSummary()), Article::getSummary, article.getSummary());
            queryWrapper.ge(Objects.nonNull(article.getCreateTime()),Article::getCreateTime,article.getCreateTime());
        }
        // 对isTop进行降序
        queryWrapper.orderByDesc(Article::getCreateTime);
        PageVo pageVo = getPageVo(pageNum, pageSize, queryWrapper);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult searchArticle(Integer pageNum, Integer pageSize, String condition) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Article::getTitle, condition).or().
                like(Article::getSummary, condition).or().
                like(Article::getContent, condition);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getCreateTime);
        PageVo pageVo = getPageVo(pageNum, pageSize, lambdaQueryWrapper);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addArticle(Article article) {
        save(article);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateArticle(Article article) {
        updateById(article);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

}

