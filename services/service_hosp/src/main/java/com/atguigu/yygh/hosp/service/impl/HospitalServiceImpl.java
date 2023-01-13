package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/7 20:50
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> parameterMap) {
        String mapString = JSONObject.toJSONString(parameterMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);

        if (hospitalExist != null) {
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setUpdateTime(new Date());
            hospital.setCreateTime(hospitalExist.getCreateTime());
        } else {
            hospital.setStatus(0);
            hospital.setUpdateTime(new Date());
            hospital.setCreateTime(new Date());
        }
        hospital.setIsDeleted(0);
        hospitalRepository.save(hospital);
    }

    @Override
    public Hospital getByHosCode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {

        Pageable pageable = PageRequest.of(page - 1, limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        List<Hospital> content = pages.getContent();
        content.stream().forEach(this::setHospitalHosType);
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital = setHospitalHosType(hospital);
        HashMap<String, Object> map = new HashMap<>();
        map.put("hospital", hospital);
        map.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        return map;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital != null) {
            return hospital.getHosname();
        }
        return null;
    }

    @Override
    public List<Hospital> findByName(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> item(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        HashMap<String, Object> result = new HashMap<>();
        result.put("hospital", hospital);
        result.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        return result;
    }

    private Hospital setHospitalHosType(Hospital item) {
        String hostype = dictFeignClient.getName("Hostype", item.getHostype()).getData().toString();
        String province = dictFeignClient.getName(item.getProvinceCode()).getData().toString();
        String city = dictFeignClient.getName(item.getCityCode()).getData().toString();
        String district = dictFeignClient.getName(item.getDistrictCode()).getData().toString();

        item.getParam().put("hostypeString", hostype);
        item.getParam().put("fullAddress", province + city + district);
        return item;
    }

}
