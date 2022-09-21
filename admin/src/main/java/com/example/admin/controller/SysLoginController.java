package com.example.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.example.common.holders.AuthenticationHolder;
import com.example.common.result.Result;
import com.example.common.utils.JwtUtils;
import com.example.system.domain.LoginDetails;
import com.example.system.domain.SysUsers;
import com.example.system.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @Classname SysLoginController
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/19 16:21
 * @Created by 16537
 */

@RestController
@RequestMapping("/sys")
public class SysLoginController {
    /**
     * 登录逻辑
     * 需要前端配合传入uuid 头
     * 以便做用户登录次数限制
     */
    @Autowired
    SysLoginService loginService;
    @PostMapping("/login")
    public Result userLogin(@RequestBody SysUsers user){

        return loginService.login(Optional.ofNullable(user.getUsername()),Optional.ofNullable(user.getPassword()));
    }
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @GetMapping("/hello")
    public Result hello(){
        return new Result().ok("hello");
    }
}
