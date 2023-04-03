package com.liuzihan.framework.constants;

public class SystemConstants {
    /**
     * 文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     * 文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     *  分类是正常分布状态
     */
    public static final String CATEGORY_STATUS_NORMAL = "1";
    /**
     * 友链状态为审核通过
     */
    public static final String LINK_STATUS_NORMAL = "1";
    /**
     * redis缓存loginUserId前缀
     */
    public static final String REDIS_PREFIX_LOGIN_USERID = "blogLogin:";
    /**
     * 请求头token属性
     */
    public static final String REQUEST_HEADER_TOKEN = "token";
    /**
     * 根评论标记
     */
    public static final int IS_ROOT_COMMENT = -1;
    /**
     * 评论类型：文章
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型：友链
     */
    public static final String LINK_COMMENT = "1";
    /**
     * redis缓存文章观看次数
     */
    public static final String REDIS_ARTICLE_VIEW_COUNT_KEY = "article:viewCount";
    /**
     * 管理员
     */
    public static final String USER_TYPE_ADMIN = "1";
    /**
     * 普通用户
     */
    public static final String USER_TYPE_NORMAL = "0";
    /**
     * 1级分类
     */
    public static final String CATEGORY_LEVEL_1 = "1";
    /**
     * 2级分类
     */
    public static final String CATEGORY_LEVEL_2 = "2";
    /**
     * 3级分类
     */
    public static final String CATEGORY_LEVEL_3 = "3";
    /**
     * 顶层分类的父ID
     */
    public static final Long CATEGORY_TOPLEVEL_PARENT_ID = -1L;
    /**
     * 用户状态正常
     */
    public static final String USER_STATUS_NORMAL = "0";
}
    