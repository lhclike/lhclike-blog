package com.lhclike.myblog.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    //自定义一个jwtToken密钥
    private static  final  String jwtToken="lhclike";
    //创建JWT令牌的方法
    public static String createToken(Long userId){
        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        //创建一个JwtBuilder实例
        JwtBuilder jwtBuilder= Jwts.builder()
                //指定用HS256签名算法，jwtToken作为密钥
                .signWith(SignatureAlgorithm.HS256,jwtToken)
                //JWT声明部分
                .setClaims(map)
                //设置JWT的签发时间
                .setIssuedAt(new Date())
                //设置过期时间这里是24小时
                .setExpiration(new Date(System.currentTimeMillis()+24 * 60 * 60 * 1000));
        //返回生成的token
        String token=jwtBuilder.compact();
        return  token;
    }
    //这个是验证token的方法
    public static Map<String,Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            //getBody是的到JWT的声明部分
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
