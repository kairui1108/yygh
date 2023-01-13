package com.atguigu.yygh.cmn.controller;

import com.atguigu.common.result.Result;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/6 9:53
 */
@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@Slf4j
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "根据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result findChildrenData(@PathVariable("id") Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    @ApiOperation(value = "导出excel文件")
    @GetMapping("/exportData")
    public void exportDict(HttpServletResponse response) {
        dictService.exportDict(response);
    }

    @ApiOperation(value = "导入excel文件")
    @PostMapping("/importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }

    @ApiOperation("根据dict_code和value查询")
    @GetMapping("getname/{dictCode}/{value}")
    public Result getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value) {
        String name = dictService.getDictName(dictCode, value);
        return Result.ok(name);
    }

    @ApiOperation("根据value查询")
    @GetMapping("/getname/{value}")
    public Result getName(@PathVariable("value") String value) {
        String name = dictService.getDictName("", value);
        return Result.ok(name);
    }

    @ApiOperation("根据dictCode获取下级节点")
    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable("dictCode") String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }


}
