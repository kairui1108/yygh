package com.atguigu.yygh.cmn.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ruikai
 * @version 1.0k
 * @date 2023/1/5 15:40
 */
@Configuration
@MapperScan("com.atguigu.yygh.cmn.mapper")
public class CmnConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
