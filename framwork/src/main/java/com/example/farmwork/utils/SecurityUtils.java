package com.example.farmwork.utils;

import com.example.common.domain.LoginDetails;
import com.example.common.domain.SysUsers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * @Classname SecurityUtils
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/21 13:22
 * @Created by 16537
 */
public class SecurityUtils {

    private static LoginDetails getLoginDetails(){
        return (LoginDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static SysUsers getLoginUser(){
        return getLoginDetails().getSysUsers();
    }
    public static String getUserToken(){
        return getLoginDetails().getToken();
    }
    public static List<String> getPermission(){
        return getLoginDetails().getPermission();
    }
    public static void setAuthentication(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
