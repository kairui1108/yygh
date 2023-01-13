package com.atguigu.yygh.hosp.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/5 15:40
 */
@Configuration
@MapperScan("com.atguigu.yygh.hosp.mapper")
public class HospitalConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
