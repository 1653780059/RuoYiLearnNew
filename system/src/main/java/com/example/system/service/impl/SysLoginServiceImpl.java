package com.example.system.service.impl;

import com.example.common.constants.RedisConstants;
import com.example.common.holders.AuthenticationHolder;
import com.example.common.result.Result;
import com.example.common.utils.JwtUtils;
import com.example.common.utils.ServletUtils;
import com.example.common.domain.LoginDetails;
import com.example.system.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @Classname SysLoginServiceImpl
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/20 21:34
 * @Created by 16537
 */
@Service
public class SysLoginServiceImpl implements SysLoginService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    AuthenticationManager authenticationManager;
    @Override
    public Result login(String username, String password,String verification) {
        String key=RedisConstants.USER_NOT_FOUND+username;
        if(stringRedisTemplate.opsForValue().get(key)!=null){
            throw new RuntimeException("用户未注册请先注册");
        }
        if(username==null||password==null){
           throw new RuntimeException("用户名密码不能为空");
        }
        if(verification==null){
            throw new RuntimeException("验证码不可为空");
        }
        String verificationKey=RedisConstants.LOGIN_VERIFICATION_PREFIX+ ServletUtils.getLoginUUID();
        String s = stringRedisTemplate.opsForValue().get(verificationKey);
        if(s==null){
            throw new RuntimeException("验证码已过期，请刷新");
        }
        if(!verification.equals(s)){
            throw new RuntimeException("验证码错误");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        AuthenticationHolder.setToken(usernamePasswordAuthenticationToken);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        LoginDetails loginDetails = (LoginDetails) authenticate.getPrincipal();
        String token = JwtUtils.getToken(loginDetails.getToken());
        return new Result().ok(token);

    }
}
