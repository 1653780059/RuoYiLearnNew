package com.example.dao.mapper;

import com.example.base.domain.SysUsers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
* @author 16537
* @description 针对表【sys_users(系统用户)】的数据库操作Mapper
* @createDate 2022-09-19 16:25:30
* @Entity com.example.base.domain.SysUsers
*/
@Mapper
public interface SysUsersMapper extends BaseMapper<SysUsers> {

    Optional<SysUsers> loadUserByUsername(@Param("username") String username);

    List<String> getPermission(@Param("userId") Integer userId);

    void setVaild(@Param("userId") Integer userId, @Param("vaild") int vaild);
}




