package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.common.exception.CustomException;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;

import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/7 20:50
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/saveHospital")
    private Result saveHosp(HttpServletRequest request) {
        Map<String, Object> parameterMap = getMap(request);

        String logoData = parameterMap.get("logoData").toString();
        logoData = logoData.replaceAll(" ", "+");
        parameterMap.put("logoData", logoData);

        hospitalService.save(parameterMap);
        return Result.ok();
    }

    @PostMapping("/hospital/show")
    private Result getHospital(HttpServletRequest request) {
        Map<String, Object> paramMap = getMap(request);
        String hoscode = paramMap.get("hoscode").toString();
        Hospital hospital = hospitalService.getByHosCode(hoscode);
        return Result.ok(hospital);
    }

    @PostMapping("/saveDepartment")
    private Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> map = getMap(request);

        departmentService.saveDepartment(map);
        return Result.ok();
    }

    @PostMapping("/department/list")
    private Result departmentList(HttpServletRequest request) {
        Map<String, Object> map = getMap(request);
        String hoscode = map.get("hoscode").toString();

        String pageString = map.get("page").toString();
        pageString = StringUtils.isEmpty(pageString) ? "1" : pageString;
        Integer page = Integer.valueOf(pageString);

        String limitString = map.get("limit").toString();
        limitString = StringUtils.isEmpty(limitString) ? "1" : limitString;
        Integer limit = Integer.valueOf(limitString);

        DepartmentQueryVo queryVo = new DepartmentQueryVo();
        queryVo.setHoscode(hoscode);
        Page<Department> pageModel = departmentService.page(page, limit, queryVo);

        return Result.ok(pageModel);
    }

    @PostMapping("/department/remove")
    private Result remove(HttpServletRequest request) {
        Map<String, Object> map = getMap(request);
        String hoscode = map.get("hoscode").toString();
        String depcode = map.get("depcode").toString();

        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    @PostMapping("/saveSchedule")
    private Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> map = getMap(request);
        scheduleService.save(map);
        return Result.ok();
    }

    @PostMapping("/schedule/list")
    private Result scheduleList(HttpServletRequest request) {
        Map<String, Object> map = getMap(request);
        String hoscode = map.get("hoscode").toString();

        String pageString = map.get("page").toString();
        pageString = StringUtils.isEmpty(pageString) ? "1" : pageString;
        Integer page = Integer.valueOf(pageString);

        String limitString = map.get("limit").toString();
        limitString = StringUtils.isEmpty(limitString) ? "1" : limitString;
        Integer limit = Integer.valueOf(limitString);

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);

        Page<Schedule> pageModel = scheduleService.page(page, limit, scheduleQueryVo);

        return Result.ok(pageModel);
    }

    @PostMapping("schedule/remove")
    private Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> map = getMap(request);
        String hoscode = map.get("hoscode").toString();
        String hosScheduleId = map.get("hosScheduleId").toString();
        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }

    private Map<String, Object> getMap(HttpServletRequest request) {
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestParameterMap);

        String sign = paramMap.get("sign").toString();

        String hoscode = paramMap.get("hoscode").toString();
        String signKey = hospitalSetService.getSignKey(hoscode);

        String key = MD5.encrypt(signKey);
        if (!key.equals(sign)) {
            throw new CustomException(ResultCodeEnum.SIGN_ERROR);
        }
        return paramMap;
    }


}
