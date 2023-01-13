package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.common.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 21:18
 */
@Api(tags = "前台接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable("page") Integer page,
                               @PathVariable("limit") Integer limit,
                               HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitals = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称查询")
    @GetMapping("findByHosName/{hosname}")
    private Result findHospByName(@PathVariable("hosname") String hosname) {
        List<Hospital> list =  hospitalService.findByName(hosname);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据医院编号查询所有科室")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable("hoscode") String hoscode) {
        List<DepartmentVo> deps = departmentService.findDeptTree(hoscode);
        return Result.ok(deps);
    }

    @ApiOperation(value = "根据医院编号获取预约挂号详情")
    @GetMapping("findHospDetail/{hoscode}")
    public Result item(@PathVariable("hoscode") String hoscode) {
        Map<String, Object> map = hospitalService.item(hoscode);
        return Result.ok(map);
    }

}
