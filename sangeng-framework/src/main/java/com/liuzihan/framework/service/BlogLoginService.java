package com.liuzihan.framework.service;

import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user,boolean isAdmin);

    ResponseResult logout();

    ResponseResult getCurrentUser();
}
