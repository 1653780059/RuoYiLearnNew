package com.example.system.service.impl;


import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.example.common.constants.RedisConstants;
import com.example.common.enums.AccountStates;
import com.example.common.enums.LoginStates;
import com.example.common.exception.MaxLoginFailException;
import com.example.common.holders.AuthenticationHolder;
import com.example.common.domain.LoginDetails;
import com.example.common.domain.SysUsers;
import com.example.system.factory.AsyncFactory;
import com.example.system.factory.AsyncManager;
import com.example.system.mapper.SysUsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    /**
     * 根据用户名获取对象
     * @param username 用户名
     * @return 根据用户名获取用户信息
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SysUsers> sysUsers = sysUsersMapper.loadUserByUsername(username);
        if (!sysUsers.isPresent()||Objects.equals(sysUsers.get().getValid(),AccountStates.OFF.getType())) {
            AsyncManager.getAsyncManager().execute(new AsyncFactory().sysLoginInfoLogTask(username, LoginStates.FAIL.getType(), "用户未注册请先注册"));
            String key=RedisConstants.USER_NOT_FOUND+username;
            stringRedisTemplate.opsForValue().set(key,"",RedisConstants.USER_NOT_FOUND_EXPIRATION_TIME,TimeUnit.MINUTES);
            throw new UsernameNotFoundException("用户名未注册请先注册");
        }
        if(Objects.equals(sysUsers.get().getValid(), AccountStates.DISABLE.getType())){
            AsyncManager.getAsyncManager().execute(new AsyncFactory().sysLoginInfoLogTask(username, LoginStates.FAIL.getType(), "用户被禁用"));
            throw new RuntimeException("用户被禁用");
        }
        String uuid = UUID.randomUUID().toString();
        String failKey = RedisConstants.LOGIN_FAIL_PREFIX+ username;
        if(stringRedisTemplate.opsForValue().get(failKey)!=null){
            if(RedisConstants.MAX_FAIL_COUNT<Integer.parseInt(stringRedisTemplate.opsForValue().get(failKey))){
                Long expire = stringRedisTemplate.getExpire(failKey);
                long l = expire / 60;
                AsyncManager.getAsyncManager().execute(new AsyncFactory().sysLoginInfoLogTask(username, LoginStates.FAIL.getType(), "用户登录次数过多被锁定"));
                throw new MaxLoginFailException("登录失败,次数过多用户已锁定,"+l+"分钟后重试");
            }
        }
        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setSysUsers(sysUsers.get());
        //重试次数限制
        if(matchPassword(AuthenticationHolder.getPassword(),sysUsers.get().getPassword(),failKey,username)){
            loginDetails.setToken(uuid);
            String key = RedisConstants.LOGIN_USER_PREFIX+loginDetails.getToken();
            loginDetails.setPermission(sysUsersMapper.getPermission(sysUsers.get().getId()));
            String jsonStr = JSONUtil.toJsonStr(loginDetails,new JSONConfig().setIgnoreNullValue(true).setIgnoreError(true));
            stringRedisTemplate.opsForValue().set(key,jsonStr,RedisConstants.LOGIN_EXPIRATION_TIME, TimeUnit.MINUTES);
        };
        return loginDetails;

    }
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 失败重试次数计数
     * @param loginDetails 用户详细信息
     * @param loginPassword 登录密码
     * @param userPassword 用户密码
     * @param key   登录失败redis计数的key
     * @param username
     */
    private Boolean matchPassword(String loginPassword, String userPassword, String key, String username) {
        boolean matches = passwordEncoder.matches(loginPassword, userPassword);

        if(!matches){

            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
                Integer i = Integer.parseInt(stringRedisTemplate.opsForValue().get(key)) + 1;
                stringRedisTemplate.opsForValue().set(key,i.toString());
            }else {
                stringRedisTemplate.opsForValue().set(key,"1");
            }
            if(RedisConstants.MAX_FAIL_COUNT<Integer.parseInt(stringRedisTemplate.opsForValue().get(key))){
                stringRedisTemplate.expire(key,RedisConstants.ALLOW_TRY_AGAIN_TIME,TimeUnit.MINUTES);
                AsyncManager.getAsyncManager().execute(new AsyncFactory().sysLoginInfoLogTask(username, LoginStates.FAIL.getType(), "登录失败,次数过多用户已锁定,十分钟后重试"));
                throw new MaxLoginFailException("登录失败,次数过多用户已锁定,十分钟后重试");
            }
        }else {
            stringRedisTemplate.delete(key);
        }

    return Boolean.TRUE.equals(matches);
    }
}
