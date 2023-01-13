package com.atguigu.yygh.user.controller;

import com.atguigu.common.result.Result;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 12:54
 */
@RestController
@Api(tags = "用户信息接口")
@RequestMapping("/api/user")
public class UserApiInfoController {

    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> map = userInfoService.login(loginVo);
        return Result.ok(map);
    }

}
