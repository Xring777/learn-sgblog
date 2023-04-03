package com.liuzihan.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author liuzihan
 * @since 2022-03-31 21:41:31
 */
public interface UserService extends IService<User> {


    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult updateUserStatus(User user);

    ResponseResult selectUser(User user, Integer pageNum, Integer pageSize);

    ResponseResult deleteUser(Long id);
}

