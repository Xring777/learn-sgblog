package com.liuzihan.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author liuzihan
 * @since 2022-03-31 20:18:05
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink(boolean isAdmin);

    ResponseResult addLink(Link link);

    ResponseResult updateLink(Link link);

    ResponseResult deleteLink(Long id);
}

