package com.example.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @Classname JwtUtils
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/20 10:57
 * @Created by 16537
 */

public class JwtUtils {
    /**
     * jwt秘钥
     */
    private static String key="wisu3i$2s@slxiggjiwalsk*woesj(ewisk";
    public static String getToken(String data){
        return JWT.create().withClaim("token", data).sign(Algorithm.HMAC256(key));
    }
    public static String parseToken(String token){
        String s=null;
        try {
            JWTVerifier build = JWT.require(Algorithm.HMAC256(key)).build();
            DecodedJWT verify = build.verify(token);
            Claim token1 = verify.getClaim("token");
            s=token1.asString();

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return s;

    }
}
