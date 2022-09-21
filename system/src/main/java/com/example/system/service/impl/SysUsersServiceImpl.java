package com.example.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.system.domain.SysUsers;
import com.example.system.service.SysUsersService;
import com.example.system.mapper.SysUsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 16537
* @description 针对表【sys_users(系统用户)】的数据库操作Service实现
* @createDate 2022-09-19 16:25:30
*/
@Service
public class SysUsersServiceImpl extends ServiceImpl<SysUsersMapper, SysUsers>
    implements SysUsersService{

}




