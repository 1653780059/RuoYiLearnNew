package com.example.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.result.Result;
import com.example.system.domain.SysMenus;
import com.example.system.domain.SysUsers;
import com.example.system.mapper.SysMenusMapper;
import com.example.system.mapper.SysUsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/menu")
public class SysMenusController extends BaseController{
    @Autowired
    SysMenusMapper sysMenusMapper;

    /**
     * 获取菜单列表
     * @return 菜单列表
     */
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @GetMapping("/list")
    public Result getList(){
        startPage();
        List<SysMenus> sysUsers = sysMenusMapper.selectList(new QueryWrapper<>());
        return new Result().ok(sysUsers);
    }
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @GetMapping("/test/{date}")
    public Result test(@PathVariable("date") Date date){
        return new Result().ok(date);
    }
}
