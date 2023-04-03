package com.liuzihan.blog.controller;

import com.liuzihan.framework.annotation.SystemLog;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @SystemLog(businessName = "获取分类列表")
    public ResponseResult getCategoryList(){
       return categoryService.getCategoryList();
    }

    @GetMapping("/getLevelCategoryList/{parentId}")
    @SystemLog(businessName = "获取分层分类列表")
    public ResponseResult getLevelCategoryList(@PathVariable Long parentId){
        return categoryService.getLevelCategoryList(parentId,false);
    }
}
