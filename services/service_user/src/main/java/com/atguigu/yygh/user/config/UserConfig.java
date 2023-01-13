package com.atguigu.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 12:53
 */
@Configuration
@MapperScan("com.atguigu.yygh.user.mapper")
public class UserConfig {
}
