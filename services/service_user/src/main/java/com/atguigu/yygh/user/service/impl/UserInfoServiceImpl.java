package com.atguigu.yygh.user.service.impl;

import com.atguigu.common.exception.CustomException;
import com.atguigu.common.helper.JwtHelper;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 12:55
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String SMS_KEY = "SMS_KEY:";

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        String code = loginVo.getCode();
        String email = loginVo.getEmail();
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)) {
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }
        String codeCache = stringRedisTemplate.opsForValue().get(SMS_KEY + email);
        if (codeCache == null) {
            throw new CustomException("验证码过期，请重新获取验证码", 201);
        }
        if (!codeCache.equals(code)) {
            throw new CustomException("验证码错误！", 403);
        }
        stringRedisTemplate.delete(SMS_KEY + email);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setName(email);
            userInfo.setNickName(email);
            userInfo.setEmail(email);
            userInfo.setStatus(1);
            baseMapper.insert(userInfo);
        }

        if (userInfo.getStatus() == 0) {
            throw new CustomException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        HashMap<String, Object> map = new HashMap<>(2);
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        map.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;

    }
}
