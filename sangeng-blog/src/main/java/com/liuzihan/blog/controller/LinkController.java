package com.liuzihan.blog.controller;

import com.liuzihan.framework.annotation.SystemLog;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @SystemLog(businessName = "获取友链列表")
    public ResponseResult getAllLink(){
        return linkService.getAllLink(false);
    }
}
