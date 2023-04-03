package com.liuzihan.blog.controller;

import com.liuzihan.framework.annotation.SystemLog;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(@RequestParam(value = "file", required = false)MultipartFile img) throws IOException {
        return uploadService.uploadImg(img);
    }
}