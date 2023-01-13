package com.atguigu.yygh.hosp.controller;

import com.atguigu.common.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 15:11
 */
@Api(tags = "医院科室管理")
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation("查询医院所有科室列表")
    @GetMapping("/getDeptList/{hoscode}")
    private Result getDeptList(@PathVariable("hoscode") String hoscode) {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }
}
