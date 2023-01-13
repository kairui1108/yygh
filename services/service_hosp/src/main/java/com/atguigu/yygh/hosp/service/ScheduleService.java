package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 10:48
 */
public interface ScheduleService {
    void save(Map<String, Object> map);

    Page<Schedule> page(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    Map<String,Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);
}
