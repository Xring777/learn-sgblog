package com.liuzihan.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.Category;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author liuzihan
 * @since 2022-03-31 11:28:38
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult getLevelCategoryList(Long parentId,boolean isAdmin);

    List<Long> getChildrenIds(Long parentId, String level,boolean isAdmin);

    ResponseResult allCategories(boolean isAdmin);

    ResponseResult addCategory(Category category);

    ResponseResult updateCategory(Category category);

    ResponseResult deleteCategory(Long id, String level);
}

