package com.liuzihan.framework.service.impl;

import com.liuzihan.framework.constants.SystemConstants;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.LoginUser;
import com.liuzihan.framework.domain.entity.User;
import com.liuzihan.framework.domain.vo.BlogUserLoginVo;
import com.liuzihan.framework.domain.vo.UserInfoVo;
import com.liuzihan.framework.service.BlogLoginService;
import com.liuzihan.framework.utils.BeanCopyUtils;
import com.liuzihan.framework.utils.JwtUtil;
import com.liuzihan.framework.utils.RedisCache;
import com.liuzihan.framework.utils.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(@NotNull User user, boolean isAdmin) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果验证失败
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //得到userId生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        if (isAdmin && !loginUser.getUser().getType().equals(SystemConstants.USER_TYPE_ADMIN)){
            throw new RuntimeException("用户权限不足");
        }
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //将用户信息存入redis
        redisCache.setCacheObject(SystemConstants.REDIS_PREFIX_LOGIN_USERID+userId,loginUser);
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userid
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject(SystemConstants.REDIS_PREFIX_LOGIN_USERID+userId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCurrentUser() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }
}
