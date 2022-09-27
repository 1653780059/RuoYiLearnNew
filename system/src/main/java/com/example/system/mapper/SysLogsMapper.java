package com.example.system.mapper;

import com.example.common.domain.SysLogs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 16537
* @description 针对表【sys_logs(系统日志)】的数据库操作Mapper
* @createDate 2022-09-27 13:57:24
* @Entity com.example.common.domain.SysLogs
*/
@Mapper
public interface SysLogsMapper extends BaseMapper<SysLogs> {

}




