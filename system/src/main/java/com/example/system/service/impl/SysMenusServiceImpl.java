package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.base.domain.SysMenus;
import com.example.system.service.SysMenusService;
import com.example.dao.mapper.SysMenusMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 16537
* @description 针对表【sys_menus(资源管理)】的数据库操作Service实现
* @createDate 2022-09-26 09:59:58
*/
@Service
public class SysMenusServiceImpl extends ServiceImpl<SysMenusMapper, SysMenus>
    implements SysMenusService{

    @Override
    public List<SysMenus> selectList() {
        return baseMapper.selectList(new QueryWrapper<>());
    }
}




