package com.liuzihan.admin.controller;

import com.liuzihan.framework.annotation.SystemLog;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.Article;
import com.liuzihan.framework.domain.entity.Category;
import com.liuzihan.framework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

//    @GetMapping("/getCategoryList")
//    @SystemLog(businessName = "获取分类列表")
//    public ResponseResult getCategoryList(){
//       return categoryService.getCategoryList();
//    }

    @GetMapping("/allCategories")
    @SystemLog(businessName = "获取所有分类列表")
    public ResponseResult allCategories(){
        return categoryService.allCategories(true);
    }

    @PostMapping("/addCategory")
    @SystemLog(businessName = "新建分类")
    public ResponseResult addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PutMapping("/updateCategory")
    @SystemLog(businessName = "修改分类")
    public ResponseResult updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/deleteCategory/{id}")
    @SystemLog(businessName = "删除分类")
    public ResponseResult deleteCategory(@PathVariable Long id,String level) {
        return categoryService.deleteCategory(id,level);
    }

}
