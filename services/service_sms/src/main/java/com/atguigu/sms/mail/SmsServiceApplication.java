package com.atguigu.sms.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 18:48
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableSwagger2
public class SmsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsServiceApplication.class);
    }
}
