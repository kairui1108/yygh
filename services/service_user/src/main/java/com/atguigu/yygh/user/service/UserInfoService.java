package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 12:53
 */

public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> login(LoginVo loginVo);
}
