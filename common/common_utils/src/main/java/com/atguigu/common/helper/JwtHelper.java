package com.atguigu.common.helper;

import com.alibaba.excel.util.StringUtils;
import io.jsonwebtoken.*;

import java.util.Date;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 13:24
 */
public class JwtHelper {
    private static long tokenExpiration = 24*60*60*1000;
    private static String tokenSignKey = "p20230110@#*";

    public static String createToken(Long userId, String userName) {
        String token = Jwts.builder()
                .setSubject("YYGH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }
    public static Long getUserId(String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
    }
    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)) {
            return "";
        }
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userName");
    }
    public static void main(String[] args) {
        String token = JwtHelper.createToken(1L, "55");
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUserName(token));
    }
}

