package com.example.admin.controller;

import com.example.common.enums.OperationType;
import com.example.common.result.Result;
import com.example.base.domain.SysUsers;
import com.example.common.annotation.Log;
import com.example.system.service.SysLoginService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author 16537
 * @Classname SysLoginController
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/19 16:21
 * @Created by 16537
 */

@RestController
@RequestMapping("/sys")
@AllArgsConstructor

public class SysLoginController {
    /**
     * 登录逻辑
     * 需要前端配合传入uuid头，以获取验证码
     * 以便做用户登录次数限制
     */

    private SysLoginService loginService;
    @PostMapping("/login")
    public Result userLogin(@RequestBody SysUsers user){

        return loginService.login(user.getUsername(),user.getPassword(),user.getVerification());
    }
    @Log(title = "测试",OPERATION_TYPE = OperationType.SELECT)
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @GetMapping("/hello")
    public Result hello(){
        return Result.success("hello");
    }
}
