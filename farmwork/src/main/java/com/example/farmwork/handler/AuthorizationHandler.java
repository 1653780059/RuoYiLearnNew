package com.example.farmwork.handler;

import com.example.farmwork.utils.SecurityUtils;
import com.example.system.domain.LoginDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname Authorization
 * @Description 授权处理器
 * @Version 1.0.0
 * @Date 2022/9/20 16:37
 * @Created by 16537
 */
@Service("a")
public class AuthorizationHandler{
    public boolean hasPer(String permission){

        List<String> permission1 = SecurityUtils.getPermission();
        List<String> collect = permission1.stream().filter(per -> per.equals(permission)).collect(Collectors.toList());
        if(collect.isEmpty()){
            return false;
        }
        return true;
    }
}
