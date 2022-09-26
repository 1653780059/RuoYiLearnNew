package com.example.common.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @Classname RequestUtils
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/21 8:52
 * @Created by 16537
 */
public class ServletUtils {
    public static ServletRequestAttributes getRequestAttributes(){
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    public static HttpServletRequest getRequest(){
        return getRequestAttributes().getRequest();
    }
    public static String getLoginUUID(){
        return getRequest().getHeader("uuid");
    }
    public static String getParam(String param){
        return getRequest().getParameter(param);
    }
    public static String getParam(String param,String defaultValue){
        String parameter = getRequest().getParameter(param);
        if(parameter==null){
            return defaultValue;
        }
        return parameter;
    }

}
