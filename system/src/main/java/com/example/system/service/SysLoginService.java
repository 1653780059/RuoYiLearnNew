package com.example.system.service;

import com.example.common.result.Result;

import java.util.Optional;

/**
 * @Classname SysLoginService
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/20 21:33
 * @Created by 16537
 */
public interface SysLoginService {
    /**
     * 登录逻辑
     * @param username 用户名
     * @param password 密码
     * @param verification 验证码
     * @return 用户登录token
     */
    Result login(String username,String password,String verification);
}
