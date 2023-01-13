package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/7 20:49
 */
public interface HospitalService {
    void save(Map<String, Object> parameterMap);

    Hospital getByHosCode(String hoscode);

    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalSetQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object> getHospById(String id);

    String getHospName(String hoscode);

    List<Hospital> findByName(String hosname);

    Map<String, Object> item(String hoscode);
}
