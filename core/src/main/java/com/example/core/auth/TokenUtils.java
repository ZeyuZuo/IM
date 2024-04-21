package com.example.core.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private static final String TOKEN_SECRET = "nxbNXiwiuafbejdcbdueduifixohafgcnhiedxgygcfhsy";
    private static final Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
    private static final long EXPIRATION_TIME = 24*60*60*1000; // 2天

    public static String generateToken (String username, String password){

        String token = "";
        try {
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("password",password)
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(algorithm);
        }catch (Exception e){
            System.out.println("token生成失败");
            return  null;
        }
        return token;
    }

    public static boolean verify(String token){
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getExpiresAt().after(new Date());
        }catch (Exception e){
            System.out.println("token验证失败");
            return  false;
        }
    }
}
