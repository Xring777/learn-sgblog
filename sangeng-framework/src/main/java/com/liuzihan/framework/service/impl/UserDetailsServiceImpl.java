package com.liuzihan.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liuzihan.framework.constants.SystemConstants;
import com.liuzihan.framework.domain.entity.LoginUser;
import com.liuzihan.framework.domain.entity.User;
import com.liuzihan.framework.enums.AppHttpCodeEnum;
import com.liuzihan.framework.exception.SystemException;
import com.liuzihan.framework.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查询到用户信息，如果没有查到则抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        if (!user.getStatus().equals(SystemConstants.USER_STATUS_NORMAL)){
            throw new RuntimeException("该用户已被封禁");
        }
        //返回用户信息
        // TODO 查询权限信息封装
        String userType = user.getType();
        List<String> list = new ArrayList<>();
        list.add(userType);
        return new LoginUser(user,list);
    }
}
