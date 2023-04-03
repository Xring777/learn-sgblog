package com.liuzihan.admin.controller;

import com.liuzihan.framework.annotation.SystemLog;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.Article;
import com.liuzihan.framework.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articleList")
    @SystemLog(businessName = "获取文章列表")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId,String level){
        return articleService.articleList(pageNum,pageSize,categoryId,level);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "查看文章详情")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        return articleService.getArticleDetail(id);
    }

    @PostMapping("/query")
    @SystemLog(businessName = "筛选文章")
    public ResponseResult selectArticle(Integer pageNum, Integer pageSize,String level ,@RequestBody Article article) {
        return articleService.selectArticle(pageNum, pageSize, article,level);
    }

    @GetMapping("/search")
    @SystemLog(businessName = "搜索文章")
    public ResponseResult searchArticle(Integer pageNum, Integer pageSize, String condition) {
        return articleService.searchArticle(pageNum, pageSize, condition);
    }

    @PostMapping("/addArticle")
    @SystemLog(businessName = "新建文章")
    public ResponseResult addArticle(@RequestBody Article article) {
        return articleService.addArticle(article);
    }

    @PutMapping("/updateArticle")
    @SystemLog(businessName = "修改文章")
    public ResponseResult updateArticle(@RequestBody Article article) {
        return articleService.updateArticle(article);
    }

    @DeleteMapping("/deleteArticle/{id}")
    @SystemLog(businessName = "删除文章")
    public ResponseResult deleteArticle(@PathVariable Long id) {
        return articleService.deleteArticle(id);
    }

}
