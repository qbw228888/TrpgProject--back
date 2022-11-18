package com.halden.TRPG.utils;


import com.mysql.cj.util.StringUtils;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {
    private static final long EXPIRATION = 1000 * 60 * 60 * 2;//过期时间两小时
    private static final String SIGN_KEY = "halden";//秘钥
    private static final String SUBJECT = "user";

    //生成token
    public static String createToken(String uid, String username){
        String token = Jwts.builder()
                .setSubject(SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .claim("uid", uid)
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS512, SIGN_KEY)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    //得到uid
    public static String getUid(String token){
        if (StringUtils.isNullOrEmpty(token)){
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SIGN_KEY).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        String uid = (String) body.get("uid");
        return uid;
    }

    //得到用户名
    public static String getUsername(String token){
        if (StringUtils.isNullOrEmpty(token)){
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SIGN_KEY).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        String username = (String) body.get("username");
        return username;
    }

    public static boolean check(String token){
        if (StringUtils.isNullOrEmpty(token)){
            return false;
        }
        Jws<Claims> claimsJws = null;
        try {
            claimsJws = Jwts.parser().setSigningKey(SIGN_KEY).parseClaimsJws(token);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        Claims body = claimsJws.getBody();
        int exp = (int) body.get("exp");
        int now = (int) System.currentTimeMillis()/1000;
        if (now < exp){
            return true;
        }
        return false;
    }
    public static void main(String[] args) {
        String token = JwtUtil.createToken("5bd38ebf97090b58ac054afa27bf2097", "test6");
        System.out.println(token);
        System.out.println(JwtUtil.getUid(token));
        System.out.println(JwtUtil.getUsername(token));
    }
}
