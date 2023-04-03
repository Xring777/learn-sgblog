package com.liuzihan.blog.controller;

import com.liuzihan.framework.annotation.SystemLog;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    @SystemLog(businessName = "获取热门文章列表")
    public ResponseResult hotArticleList(){
        return articleService.hotArticleList();
    }

    @GetMapping("/topArticleList")
    @SystemLog(businessName = "获取置顶文章列表")
    public ResponseResult topArticleList(){
        return articleService.topArticleList();
    }

    @GetMapping("/articleList")
    @SystemLog(businessName = "获取文章列表")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId,String level){
        return articleService.articleList(pageNum,pageSize,categoryId,level);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "查看文章详情")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    @SystemLog(businessName = "更新观看次数")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

    @GetMapping("/search")
    @SystemLog(businessName = "搜索文章")
    public ResponseResult searchArticle(Integer pageNum, Integer pageSize, String condition) {
        return articleService.searchArticle(pageNum, pageSize, condition);
    }
}
