package com.atguigu.yygh.oss.controller;

import com.atguigu.common.result.Result;
import com.atguigu.yygh.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/11 20:45
 */
@Api(tags = "文件接口")
@RestController
@Slf4j
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Resource
    private FileService fileService;

    @ApiOperation("文件上传接口")
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile multipartFile) {
        log.info("upload file {}", multipartFile.getOriginalFilename());
        String url = fileService.upload(multipartFile);
        return Result.ok(url);
    }

}
