package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/5 15:28
 */
public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);
}
