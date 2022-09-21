package com.example.system.service.impl;

import cn.hutool.json.JSONUtil;
import com.example.common.constants.RedisConstants;
import com.example.common.holders.AuthenticationHolder;
import com.example.common.result.Result;
import com.example.common.utils.JwtUtils;
import com.example.system.domain.LoginDetails;
import com.example.system.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Result login(Optional<String> username, Optional<String> password) {
        if(!username.isPresent()||!password.isPresent()){
           throw new RuntimeException("用户名密码不能为空");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username.get(),password.get());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        AuthenticationHolder.setToken(usernamePasswordAuthenticationToken);
        LoginDetails loginDetails = (LoginDetails) authenticate.getPrincipal();
        String token = JwtUtils.getToken(loginDetails.getToken());
        String key = RedisConstants.LOGIN_USER_PREFIX+loginDetails.getToken();
        String jsonStr = JSONUtil.toJsonStr(loginDetails);
        stringRedisTemplate.opsForValue().set(key,jsonStr);
        return new Result().ok(token);

    }
}
