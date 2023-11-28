package com.lhclike.myblog.config;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static  final  String jwtToken="lhclike";
    public static String createToken(Long userId){
        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        JwtBuilder jwtBuilder= Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtToken)
                .setClaims(map)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+24 * 60 * 60 * 1000));
        String token=jwtBuilder.compact();
        return  token;
    }
    public static Map<String,Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
