package com.liuzihan.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.Article;

/**
 * 文章表(Article)表服务接口
 *
 * @author liuzihan
 * @since 2022-03-31 11:28:38
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId,String level);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult selectArticle(Integer pageNum, Integer pageSize,Article article,String level);

    ResponseResult searchArticle(Integer pageNum, Integer pageSize,String condition);

    ResponseResult addArticle(Article article);

    ResponseResult updateArticle(Article article);

    ResponseResult deleteArticle(Long id);

    ResponseResult topArticleList();

}

