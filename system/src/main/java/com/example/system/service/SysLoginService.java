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
    Result login(Optional<String> username, Optional<String> password,Optional<String> verification);
}
