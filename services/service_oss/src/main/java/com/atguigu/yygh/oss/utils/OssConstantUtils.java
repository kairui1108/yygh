package com.atguigu.yygh.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/11 20:51
 */
@Component
public class OssConstantUtils implements InitializingBean {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.secret}")
    private String secret;

    @Value("${aliyun.oss.bucket}")
    private String bucket;

    public static String ENDPOINT;
    public static String ACCESS_KEY_ID;
    public static String SECRET;
    public static String BUCKET;

    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = this.endpoint;
        ACCESS_KEY_ID = this.accessKeyId;
        SECRET = this.secret;
        BUCKET = this.bucket;
    }
}
