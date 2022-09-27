package com.example.system.aspect;

import cn.hutool.core.net.Ipv4Util;
import com.example.common.domain.SysLogs;
import com.example.common.domain.SysUsers;
import com.example.common.enums.OperationType;
import com.example.common.utils.IpUtils;
import com.example.common.utils.ServletUtils;
import com.example.farmwork.utils.SecurityUtils;
import com.example.system.mapper.SysLogsMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.system.annotation.Log;

import java.util.Arrays;
import java.util.Date;

/**
 * @author 16537
 * @Classname LoggerAsepct
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/27 13:31
 */
@Aspect
@Component
public class LoggerAspect {
    @Autowired
    SysLogsMapper sysLogsMapper;
    @AfterReturning(pointcut = "@annotation(log)",returning = "result")
    public void afterReturning(JoinPoint joinPoint,Log log,Object result){
        handleLog(joinPoint,log,null,result);
    }
    @AfterThrowing(pointcut = "@annotation(log)",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint,Log log,Exception e){
        handleLog(joinPoint,log,e,null);
    }
    private void handleLog(JoinPoint joinPoint, Log log, Exception e, Object result) {
        SysUsers loginUser = SecurityUtils.getLoginUser();
        SysLogs sysLogs = new SysLogs();
        sysLogs.setUsername(loginUser.getUsername());
        String className=joinPoint.getTarget().getClass().getName();
        String methodName=joinPoint.getSignature().getName();
        sysLogs.setMethod(className+methodName);
        Object[] args = joinPoint.getArgs();
        sysLogs.setParams(Arrays.toString(args));
        OperationType operationType = log.OPERATION_TYPE();
        sysLogs.setOperation(operationType.getType());
        sysLogs.setTime(new Date().getTime());
        if (e!=null){
            sysLogs.setOperation(OperationType.FAIL.getType());
        }
        String ipAddr = IpUtils.getIpAddr(ServletUtils.getRequest());
        sysLogs.setIp(ipAddr);
        sysLogsMapper.insert(sysLogs);

    }
}
