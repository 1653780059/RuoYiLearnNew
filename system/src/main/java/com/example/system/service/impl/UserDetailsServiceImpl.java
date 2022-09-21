package com.example.system.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.example.common.constants.RedisConstants;
import com.example.common.enums.AccountStates;
import com.example.common.exception.MaxLoginFailException;
import com.example.common.holders.AuthenticationHolder;
import com.example.common.utils.ServletUtils;
import com.example.system.domain.LoginDetails;
import com.example.system.domain.SysUsers;
import com.example.system.mapper.SysUsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Classname UserDetailsServiceImpl
 * @Description 根据用户名获取用户对象和权限
 * @Version 1.0.0
 * @Date 2022/9/19 16:26
 * @Created by 16537
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    SysUsersMapper sysUsersMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SysUsers> sysUsers = sysUsersMapper.loadUserByUsername(username);
        if (!sysUsers.isPresent()||Objects.equals(sysUsers.get().getValid(),AccountStates.OFF.getType())) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if(Objects.equals(sysUsers.get().getValid(), AccountStates.DISABLE.getType())){
          throw new RuntimeException("用户被禁用");
        }
        String failKey = RedisConstants.LOGIN_FAIL_PREFIX+ ServletUtils.getLoginUUID();
        if(stringRedisTemplate.opsForValue().get(failKey)!=null){
            if(RedisConstants.MAX_FAIL_COUNT<Integer.parseInt(stringRedisTemplate.opsForValue().get(failKey))){
                Long expire = stringRedisTemplate.getExpire(failKey);
                long l = expire / 60;
                throw new MaxLoginFailException("登录失败,次数过多用户已锁定,"+l+"分钟后重试");
            }
        }
        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setSysUsers(sysUsers.get());
        loginDetails.setToken(UUID.randomUUID().toString());
        //重试次数限制
        if(matchPassword(AuthenticationHolder.getPassword(),sysUsers.get().getPassword(),loginDetails,failKey)){

            String key = RedisConstants.LOGIN_USER_PREFIX+loginDetails.getToken();
            String jsonStr = JSONUtil.toJsonStr(loginDetails);
            stringRedisTemplate.opsForValue().set(key,jsonStr,20, TimeUnit.MINUTES);
        };
        //转成json
        loginDetails.setPermission(sysUsersMapper.getPermission(sysUsers.get().getId()));

        return loginDetails;

    }
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 失败重试次数计数
     * @param loginPassword 登录密码
     * @param userPassword 用户密码
     * @param loginDetails 用户详细信息
     * @param key   登录失败redis计数的key
     */
    private Boolean matchPassword(String loginPassword, String userPassword, LoginDetails loginDetails,String key) {
        boolean matches = passwordEncoder.matches(loginPassword, userPassword);

        if(!matches){

            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
                Integer i = Integer.parseInt(stringRedisTemplate.opsForValue().get(key)) + 1;
                stringRedisTemplate.opsForValue().set(key,i.toString());
            }else {
                stringRedisTemplate.opsForValue().set(key,"1");
            }
            if(RedisConstants.MAX_FAIL_COUNT<Integer.parseInt(stringRedisTemplate.opsForValue().get(key))){
                stringRedisTemplate.expire(key,10,TimeUnit.MINUTES);
                throw new MaxLoginFailException("登录失败,次数过多用户已锁定,十分钟后重试");
            }
        }else {
            stringRedisTemplate.delete(key);
        }

    return Boolean.TRUE.equals(matches);
    }
}
