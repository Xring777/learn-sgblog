package com.liuzihan.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author liuzihan
 * @since 2022-04-11 23:35:55
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

