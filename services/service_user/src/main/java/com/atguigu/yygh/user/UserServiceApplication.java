package com.atguigu.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 12:50
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.atguigu")
@EnableSwagger2
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class);
    }
}
