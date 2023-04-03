package com.liuzihan.blog.filter;

import com.alibaba.fastjson.JSON;
import com.liuzihan.framework.constants.SystemConstants;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.LoginUser;
import com.liuzihan.framework.enums.AppHttpCodeEnum;
import com.liuzihan.framework.utils.JwtUtil;
import com.liuzihan.framework.utils.RedisCache;
import com.liuzihan.framework.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中token
        String token = httpServletRequest.getHeader(SystemConstants.REQUEST_HEADER_TOKEN);
        if (!StringUtils.hasText(token)){
            //说明没有登录，直接放行
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            //如果token超时或非法
            e.printStackTrace();
            //返回错误信息 需要登录
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
            return;
        }
        //得到userId
        String userId = claims.getSubject();
        //查询Redis得到用户信息
        LoginUser loginUser = redisCache.getCacheObject(SystemConstants.REDIS_PREFIX_LOGIN_USERID + userId);
        //判断LoginUser是否存在
        if (Objects.isNull(loginUser)) {
            //返回错误信息 需要登录
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
