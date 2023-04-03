package com.liuzihan.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuzihan.framework.constants.SystemConstants;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.Article;
import com.liuzihan.framework.domain.vo.CategoryVo;
import com.liuzihan.framework.mapper.CategoryMapper;
import com.liuzihan.framework.domain.entity.Category;
import com.liuzihan.framework.service.ArticleService;
import com.liuzihan.framework.service.CategoryService;
import com.liuzihan.framework.utils.BeanCopyUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author liuzihan
 * @since 2022-03-31 11:29:40
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表  状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream().
                filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult getLevelCategoryList(Long parentId,boolean isAdmin) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getPid, parentId);
        queryWrapper.eq(!isAdmin,Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categories = list(queryWrapper);
        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVoList);
    }

    public List<Long> getChildrenIds(Long parentId, String level,boolean isAdmin) {
        if (level.equals(SystemConstants.CATEGORY_LEVEL_1)) {
            List<Long> result = new ArrayList<>();
            List<List<Long>> lists = getNextLevelIds(parentId,isAdmin).stream()
                    .map(id -> getNextLevelIds(id,isAdmin))
                    .collect(Collectors.toList());
            for (List<Long> list : lists) {
                result.addAll(list);
            }
            return result;
        } else if (level.equals(SystemConstants.CATEGORY_LEVEL_2)) {
            return getNextLevelIds(parentId,isAdmin);
        }
        return Collections.singletonList(parentId);
    }

    @NotNull
    private List<Long> getNextLevelIds(Long parentId,boolean isAdmin) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getPid, parentId);
        queryWrapper.eq(!isAdmin,Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categories = list(queryWrapper);
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }


    @Override
    public ResponseResult allCategories(boolean isAdmin) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!isAdmin,Category::getStatus,SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categories = list();
        List<CategoryVo> first = categories.stream()
                .filter(category -> Objects.equals(category.getPid(), SystemConstants.CATEGORY_TOPLEVEL_PARENT_ID))
                .map(category -> BeanCopyUtils.copyBean(category, CategoryVo.class))
                .map(categoryVo -> categoryVo.setValue(SystemConstants.CATEGORY_LEVEL_1 + "," + categoryVo.getId()))
                .collect(Collectors.toList());
        for (CategoryVo categoryVo : first) {
            List<CategoryVo> second = categories.stream()
                    .filter(category -> Objects.equals(category.getPid(), categoryVo.getId()))
                    .map(category -> BeanCopyUtils.copyBean(category, CategoryVo.class))
                    .map(secondVo -> secondVo.setValue(SystemConstants.CATEGORY_LEVEL_2 + "," + secondVo.getId()))
                    .collect(Collectors.toList());
            categoryVo.setChildren(second);
            for (CategoryVo vo : second) {
                List<CategoryVo> third = categories.stream()
                        .filter(category -> Objects.equals(category.getPid(), vo.getId()))
                        .map(category -> BeanCopyUtils.copyBean(category, CategoryVo.class))
                        .map(thirdVo -> thirdVo.setValue(SystemConstants.CATEGORY_LEVEL_3 + "," + thirdVo.getId()))
                        .collect(Collectors.toList());
                vo.setChildren(third);
            }
        }
        return ResponseResult.okResult(first);
    }

    @Override
    public ResponseResult addCategory(Category category) {
        save(category);
        return ResponseResult.okResult(category);
    }

    @Override
    public ResponseResult updateCategory(Category category) {
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id, String level) {
        List<Long> ids = getChildrenIds(id,level,true);
        ids.add(id);
        removeByIds(ids);
        return ResponseResult.okResult();
    }


}

