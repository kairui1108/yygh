package com.atguigu.yygh.user.controller.api;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.exception.CustomException;
import com.atguigu.common.helper.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.HttpClientUtils;
import com.atguigu.yygh.user.utils.WxPropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/11 11:25
 */
@Api(tags = "微信接口")
@Controller
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class WxApiController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation(value = "返回生成二维码需要的参数")
    @GetMapping("/getLoginParam")
    @ResponseBody
    public Result qrContent() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appid", WxPropertiesUtil.WX_OPEN_APP_ID);
        map.put("scope", "snsapi_login");
        String url = WxPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            url = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.toString());
            return null;
        }
        map.put("redirect_url", url);
        map.put("state", System.currentTimeMillis());
        return Result.ok(map);
    }

    /**
     * 微信登录回调
     *
     * @param code
     * @param state
     * @return
     */
    @RequestMapping("callback")
    public String callback(String code, String state) {
        //获取授权临时票据
        System.out.println("微信授权服务器回调。。。。。。");
        System.out.println("state = " + state);
        System.out.println("code = " + code);

        if (StringUtils.isEmpty(state) || StringUtils.isEmpty(code)) {
            log.error("非法回调请求");
            throw new CustomException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //使用code和appid以及appscrect换取access_token
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                WxPropertiesUtil.WX_OPEN_APP_ID,
                WxPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        System.out.println("使用code换取的access_token结果 = " + result);

        JSONObject resultJson = JSONObject.parseObject(result);
        if(resultJson.getString("errcode") != null){
            log.error("获取access_token失败：" + resultJson.getString("errcode") + resultJson.getString("errmsg"));
            throw new CustomException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        String accessToken = resultJson.getString("access_token");
        String openId = resultJson.getString("openid");
        log.info(accessToken);
        log.info(openId);

        //根据access_token获取微信用户的基本信息
        //先根据openid进行数据库查询
        // UserInfo userInfo = userInfoService.getByOpenid(openId);
        // 如果没有查到用户信息,那么调用微信个人信息获取的接口
        // if(null == userInfo){
        //如果查询到个人信息，那么直接进行登录
        //使用access_token换取受保护的资源：微信的个人信息
        String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openId);
        String resultUserInfo = null;
        try {
            resultUserInfo = HttpClientUtils.get(userInfoUrl);
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        System.out.println("使用access_token获取用户信息的结果 = " + resultUserInfo);

        JSONObject resultUserInfoJson = JSONObject.parseObject(resultUserInfo);
        if(resultUserInfoJson.getString("errcode") != null){
            log.error("获取用户信息失败：" + resultUserInfoJson.getString("errcode") + resultUserInfoJson.getString("errmsg"));
            throw new CustomException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }

        //解析用户信息
        String nickname = resultUserInfoJson.getString("nickname");
        String headimgurl = resultUserInfoJson.getString("headimgurl");

        UserInfo userInfo = new UserInfo();
        userInfo.setOpenid(openId);
        userInfo.setNickName(nickname);
        userInfo.setStatus(1);
        userInfoService.save(userInfo);
        // }

        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getEmail();
        }
        map.put("name", name);
        if(StringUtils.isEmpty(userInfo.getEmail())) {
            map.put("openid", userInfo.getOpenid());
        } else {
            map.put("openid", "");
        }
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return "redirect:" + WxPropertiesUtil.YYGH_BASE_URL + "/weixin/callback?token="+map.get("token")+"&openid="+map.get("openid")+"&name="+URLEncoder.encode((String)map.get("name"));
    }


}
