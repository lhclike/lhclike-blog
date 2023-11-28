package com.lhclike.myblog.api;

import com.lhclike.myblog.config.MinioConfig;
import com.lhclike.myblog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    MinioConfig minioConfig;

    @PostMapping
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        String path = minioConfig.putObject(file);
        if (path!=null){
            return Result.success(path);
        }
        return Result.fail(20001,"上传失败");
    }
}