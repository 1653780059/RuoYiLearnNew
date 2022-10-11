package com.example.admin.controller;

import com.example.common.result.Result;
import com.example.base.domain.SysMenus;
import com.example.system.service.SysMenusService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 16537
 * @Classname SysMenusController
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 9:35
 */
@RestController
@RequestMapping("/sys/menu")
@AllArgsConstructor

public class SysMenusController extends BaseController{
    private SysMenusService menusService;

    /**
     * 获取菜单列表
     * @return 菜单列表
     */
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @GetMapping("/list")
    public Result getList(){
        startPage();
        List<SysMenus> sysUsers = menusService.selectList();
        return Result.success(sysUsers);
    }
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @GetMapping("/test/{date}")
    public Result test(@PathVariable("date") Date date){
        return Result.success(date);
    }
}
