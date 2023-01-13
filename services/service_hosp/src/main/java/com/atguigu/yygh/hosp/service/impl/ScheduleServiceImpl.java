package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 10:48
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void save(Map<String, Object> map) {
        String jsonString = JSONObject.toJSONString(map);
        Schedule schedule = JSONObject.parseObject(jsonString, Schedule.class);

        Schedule scheduleExist =
                scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());

        schedule.setUpdateTime(new Date());
        schedule.setCreateTime(new Date());
        schedule.setIsDeleted(0);
        schedule.setStatus(1);
        if (scheduleExist != null) {
            schedule.setCreateTime(scheduleExist.getCreateTime());
            schedule.setIsDeleted(scheduleExist.getIsDeleted());
            schedule.setStatus(scheduleExist.getStatus());
        }
        scheduleRepository.save(schedule);

    }

    @Override
    public Page<Schedule> page(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);

        Pageable pageable = PageRequest.of(page, limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Schedule> example = Example.of(schedule, matcher);
        return scheduleRepository.findAll(example, pageable);
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule == null) {
            return;
        }
        scheduleRepository.deleteById(schedule.getId());
    }

    /**
     * 聚合数据
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    @Override
    public Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("reservedNumber").as("reserveNumber")
                        .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.DESC, "workDate"),
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );

        AggregationResults<BookingScheduleRuleVo> aggResult = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> list = aggResult.getMappedResults();

        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResult =
                mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResult.getMappedResults().size();

        for (BookingScheduleRuleVo bookingScheduleRuleVo : list) {
            Date date = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }
        HashMap<String, Object> result = new HashMap<>(3);
        result.put("total", total);
        result.put("bookingScheduleRuleList", list);

        String hosname = hospitalService.getHospName(hoscode);
        Map<String, String> basemap = new HashMap<>(1);
        basemap.put("hosname", hosname);
        result.put("baseMap", basemap);

        return result;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
        List<Schedule> list =
                scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        list.stream().forEach(this::packageSchedule);
        return list;
    }

    private void packageSchedule(Schedule schedule) {
        schedule.getParam().put("hosname", hospitalService.getHospName(schedule.getHoscode()));
        schedule.getParam().put("depcode", departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        schedule.getParam().put("dayOfWeek", getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }


    /**
     * 根据日期获取周几数据
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }


}
