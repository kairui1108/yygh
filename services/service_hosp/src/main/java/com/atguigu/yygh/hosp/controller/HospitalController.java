package com.atguigu.yygh.hosp.controller;

import com.atguigu.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 11:55
 */
@Api(tags = "医院管理")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/list/{page}/{limit}")
    private Result listHosp(@PathVariable("page") Integer page,
                            @PathVariable("limit") Integer limit,
                            HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("更新医院上线状态")
    @GetMapping("/updateHospStatus/{id}/{status}")
    private Result updateStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    @ApiOperation("医院详情信息")
    @GetMapping("/hospInfo/{id}")
    private Result hospInfo(@PathVariable("id") String id) {
        Map<String, Object> map = hospitalService.getHospById(id);
        return Result.ok(map);
    }

}
