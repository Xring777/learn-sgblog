package com.liuzihan.admin.controller;

import com.liuzihan.framework.annotation.SystemLog;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.Link;
import com.liuzihan.framework.service.LinkService;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @SystemLog(businessName = "获取友链列表")
    public ResponseResult getAllLink(){
        return linkService.getAllLink(true);
    }

    @PostMapping("/addLink")
    @SystemLog(businessName = "添加友链")
    public ResponseResult addLink(@RequestBody Link link){
        return linkService.addLink(link);
    }

    @PutMapping("/updateLink")
    @SystemLog(businessName = "修改友链")
    public ResponseResult updateLink(@RequestBody Link link){
        return linkService.updateLink(link);
    }

    @DeleteMapping("/deleteLink/{id}")
    @SystemLog(businessName = "删除友链")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }
}
