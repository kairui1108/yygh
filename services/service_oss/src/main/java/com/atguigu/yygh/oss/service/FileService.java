package com.atguigu.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/11 20:47
 */

public interface FileService {
    String upload(MultipartFile multipartFile);
}
