package com.liuzihan.framework.service.impl;

import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.enums.AppHttpCodeEnum;
import com.liuzihan.framework.exception.SystemException;
import com.liuzihan.framework.service.UploadService;
import com.liuzihan.framework.utils.PathUtils;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class OssUploadService implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) throws IOException {
        //判断文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //对原始文件名进行判断
        if(!PathUtils.isImageFileName(originalFilename)){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //如果判断通过上传文件到OSS
        String filePath = "production_training/" + PathUtils.generateFilePath(originalFilename);
//        System.out.println(filePath);
        String url = uploadOss(img,filePath);//  2099/2/3/wqeqeqe.png
        return ResponseResult.okResult(url);
    }

    private String uploadOss(MultipartFile imgFile, String filePath) throws IOException {
        String endPoint = "obs.cn-south-1.myhuaweicloud.com";
        String ak = "TW0BEC1GDBIFBO5VAUEK";
        String sk = "raBehb3EIsc58rPVebDWdYKvwpW5jYu4SGOwOsNF";
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        InputStream fis = imgFile.getInputStream();
        PutObjectResult result = obsClient.putObject("liuzihan-pics", filePath, fis);
        obsClient.close();
        return result.getObjectUrl();
    }
}
