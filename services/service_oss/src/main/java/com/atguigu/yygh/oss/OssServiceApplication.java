package com.atguigu.yygh.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/11 20:35
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableSwagger2
@EnableDiscoveryClient
@ComponentScan("com.atguigu")
public class OssServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssServiceApplication.class);
    }
}
