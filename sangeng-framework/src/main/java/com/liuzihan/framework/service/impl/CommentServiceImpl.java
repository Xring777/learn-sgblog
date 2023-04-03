package com.liuzihan.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuzihan.framework.constants.SystemConstants;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.vo.CommentVo;
import com.liuzihan.framework.domain.vo.PageVo;
import com.liuzihan.framework.enums.AppHttpCodeEnum;
import com.liuzihan.framework.exception.SystemException;
import com.liuzihan.framework.mapper.CommentMapper;
import com.liuzihan.framework.domain.entity.Comment;
import com.liuzihan.framework.service.CommentService;
import com.liuzihan.framework.service.UserService;
import com.liuzihan.framework.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author liuzihan
 * @since 2022-04-11 23:35:55
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        // 根据 articleId 查询文章评论
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId, articleId);
        // 根评论 rootId 为 -1
        wrapper.eq(Comment::getRootId, SystemConstants.IS_ROOT_COMMENT);
        // 文章类型
        wrapper.eq(Comment::getType,commentType);
        wrapper.orderByAsc(Comment::getCreateTime);

        // 使用 page 封装
        Page page = new Page<Comment>(pageNum, pageSize);
        page(page, wrapper);

        // 遍历集合将 comment 封装为 commentVo
        List commentVoList = toCommentVoList(page.getRecords());

        // 使用 pageVo 返回给前端
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 封装为commentVoList
     * @param commentList 评论列表
     * @return commentVoList
     */
    public List<CommentVo> toCommentVoList(List<Comment> commentList) {
        return commentList.stream()
                .map(this::toCommentVo)
                .map(this::setChildrenComment)
                .collect(Collectors.toList());
    }

    /**
     * 设置子评论
     * @param commentVo commentVo
     * @return comment
     */
    private CommentVo setChildrenComment(CommentVo commentVo) {
        // 查询 toCommentId 等于 id 的评论
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getRootId, commentVo.getId());
//        wrapper.orderByDesc(Comment::getCreateTime);
        wrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> commentList = list(wrapper);
        // 将子评论封装为 commentVoList 并设置到 children中
        List<CommentVo> commentVoList = commentList.stream()
                .map(this::toCommentVo)
                .collect(Collectors.toList());
        commentVo.setChildren(commentVoList);
        return commentVo;
    }

    /**
     * 封装为 commentVo
     * @param comment comment
     * @return comment
     */
    public CommentVo toCommentVo(Comment comment) {
        // 拷贝 comment 给 commentVo
        CommentVo commentVo = BeanCopyUtils.copyBean(comment, CommentVo.class);

        //通过createBy查询用户的昵称并赋值
        String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
        commentVo.setUsername(nickName);
        //通过toCommentUserId查询用户的昵称并赋值
        //如果toCommentUserId不为-1才进行查询
        if (commentVo.getToCommentUserId() != SystemConstants.IS_ROOT_COMMENT) {
            String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
            commentVo.setToCommentUserName(toCommentUserName);
        }
        return commentVo;
    }
}

