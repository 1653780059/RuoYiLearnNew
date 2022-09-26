package com.example.common.holders;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @Classname AuthenticationHolder
 * @Description 登录令牌保存
 * @Version 1.0.0
 * @Date 2022/9/20 9:36
 * @Created by 16537
 */
public class AuthenticationHolder {
    private static final ThreadLocal<UsernamePasswordAuthenticationToken> LOGIN_DETAILS_THREAD_LOCAL = new ThreadLocal<>();
    public static String getPassword(){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = LOGIN_DETAILS_THREAD_LOCAL.get();
        if(usernamePasswordAuthenticationToken!=null){
            return usernamePasswordAuthenticationToken.getCredentials().toString();
        }
        return "";

    }
    public static void setToken(UsernamePasswordAuthenticationToken loginDetails){
        LOGIN_DETAILS_THREAD_LOCAL.set(loginDetails);
    }
}
