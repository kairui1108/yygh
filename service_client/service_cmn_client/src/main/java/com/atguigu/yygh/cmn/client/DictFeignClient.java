package com.atguigu.yygh.cmn.client;

import com.atguigu.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/9 12:52
 */
@FeignClient("service-cmn")
@Repository
public interface DictFeignClient {

    @ApiOperation("根据dict_code和value查询")
    @GetMapping("/admin/cmn/dict/getname/{dictCode}/{value}")
    Result getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value);

    @ApiOperation("根据value查询")
    @GetMapping("/admin/cmn/dict/getname/{value}")
    Result getName(@PathVariable("value") String value);

}
