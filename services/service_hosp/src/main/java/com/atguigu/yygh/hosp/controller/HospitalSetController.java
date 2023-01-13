package com.atguigu.yygh.hosp.controller;

import com.alibaba.excel.util.StringUtils;
import com.atguigu.common.exception.CustomException;
import com.atguigu.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/5 15:33
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@Slf4j
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取所有的医院设置信息")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public Result removeHospById(@PathVariable("id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "条件查询带分页")
    @PostMapping("findPage/{current}/{limit}")
    public Result findPageHostSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> page = new Page<>(current, limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }

        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hoscode)) {
            wrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
        }

        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);

        return Result.ok(hospitalSetPage);
    }

    @ApiOperation("添加医院信息")
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 1 可以使用 0 不可使用
        hospitalSet.setStatus(1);
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        boolean flag = hospitalSetService.save(hospitalSet);
        return flag ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据id获取医院设置")
    @GetMapping("/getHostSet/{id}")
    public Result getHospById(@PathVariable("id") Long id) {
        HospitalSet hosp = hospitalSetService.getById(id);
        return Result.ok(hosp);
    }

    @ApiOperation("修改医院设置")
    @PostMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        return flag ? Result.ok(): Result.fail();
    }

    @ApiOperation("批量删除医院设置")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean flag = hospitalSetService.removeByIds(idList);
        return flag ? Result.ok(): Result.fail();
    }

    @ApiOperation("修改医院设置锁定状态")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable("id") Long id,
                                  @PathVariable("status") Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    @ApiOperation("发送签名密钥")
    @PutMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable("id") String id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //todo: 发送短信
        return Result.ok();
    }

}
