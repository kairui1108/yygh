package com.atguigu.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.common.exception.CustomException;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.yygh.oss.service.FileService;
import com.atguigu.yygh.oss.utils.OssConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/11 20:48
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file) {
        String endpoint = OssConstantUtils.ENDPOINT;
        String accessKeyId = OssConstantUtils.ACCESS_KEY_ID;
        String secret = OssConstantUtils.SECRET;
        String bucket = OssConstantUtils.BUCKET;

        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, secret);
        try {
            InputStream inputStream = file.getInputStream();
            String origin = file.getOriginalFilename();
            int index = origin.lastIndexOf(".");
            String suffix = null;
            if (index > 0) {
                suffix = origin.substring(index + 1);
            }
            String prefix = new DateTime().toString("yyyy/MM/dd");
            String fileName = prefix + "/" + Long.toString(System.currentTimeMillis()) + "." + suffix;
            oss.putObject(bucket, fileName, inputStream);
            return String.format("https://%s.oss-cn-hangzhou.aliyuncs.com/%s", bucket, fileName);
        } catch (IOException e) {
            throw new CustomException(ResultCodeEnum.FAIL);
        }
    }
}
