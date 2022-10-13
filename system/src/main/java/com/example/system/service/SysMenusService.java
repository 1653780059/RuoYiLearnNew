package com.example.system.service;

import com.example.base.domain.SysMenus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 16537
* @description 针对表【sys_menus(资源管理)】的数据库操作Service
* @createDate 2022-09-26 09:59:58
*/
public interface SysMenusService extends IService<SysMenus> {
    /**
     * 获取菜单列表支持分页
     * @return 菜单列表
     */
    List<SysMenus> selectList();
}
