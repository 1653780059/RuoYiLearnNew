package com.example.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.base.domain.SysLogs;
import com.example.system.service.SysLogsService;
import com.example.dao.mapper.SysLogsMapper;
import org.springframework.stereotype.Service;

/**
* @author 16537
* @description 针对表【sys_logs(系统日志)】的数据库操作Service实现
* @createDate 2022-09-27 13:57:24
*/
@Service
public class SysLogsServiceImpl extends ServiceImpl<SysLogsMapper, SysLogs>
    implements SysLogsService{

}




