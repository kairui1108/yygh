package com.atguigu.yygh.hosp.controller;

import com.atguigu.common.result.Result;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 15:56
 */
@Api(tags = "排班管理")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable("page") Integer page,
                                  @PathVariable("limit") Integer limit,
                                  @PathVariable("hoscode") String hoscode,
                                  @PathVariable("depcode") String depcode) {
        Map<String, Object> map = scheduleService.getScheduleRule(page, limit, hoscode, depcode);
        return Result.ok(map);
    }

    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleInfo/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable("hoscode") String hoscode,
                                    @PathVariable("depcode") String depcode,
                                    @PathVariable("workDate") String workDate) {
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode, depcode, workDate);
        return Result.ok(list);
    }
}
