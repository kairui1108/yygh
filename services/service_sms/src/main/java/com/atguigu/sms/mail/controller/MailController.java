package com.atguigu.sms.mail.controller;

import cn.hutool.core.util.RandomUtil;
import com.atguigu.common.result.Result;
import com.atguigu.sms.mail.service.MailService;
import com.atguigu.yygh.common.utils.RegexUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 18:55
 */
@Api(tags = "邮件发送接口")
@RestController
@RequestMapping("/api/sms")
public class MailController {

    @Resource
    private MailService mailService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String SMS_KEY = "SMS_KEY:";

    @ApiOperation(value = "发送邮件")
    @GetMapping("/sendMail/{to}")
    public Result sendMail(@PathVariable("to") String to) {
        if (RegexUtils.isEmailInvalid(to)) {
            return Result.fail().message("邮箱非法");
        }
        String code = stringRedisTemplate.opsForValue().get(SMS_KEY + to);
        if (!StringUtils.isEmpty(code)) {
            return Result.ok("已经获取过验证码");
        }
        code = RandomUtil.randomNumbers(6);
        boolean flag = mailService.sendMail(to, code);
        if (flag) {
            stringRedisTemplate.opsForValue().set(SMS_KEY + to, code, 2, TimeUnit.MINUTES);
            return Result.ok("验证码发送成功");
        }
        return Result.fail().message("发送验证码失败");
    }

}
