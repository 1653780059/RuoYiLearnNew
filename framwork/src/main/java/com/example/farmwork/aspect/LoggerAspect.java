package com.example.farmwork.aspect;

import com.example.farmwork.factory.AsyncFactory;
import com.example.farmwork.factory.AsyncManager;
import com.example.dao.mapper.SysLogsMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.common.annotation.Log;

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

    /**
     * 异步线程保存日志
     * @param joinPoint 切入点
     * @param log 日志对象
     * @param e 异常对象
     * @param result 请求返回值
     */
    private void handleLog(JoinPoint joinPoint, Log log, Exception e, Object result) {
        /*SysUsers loginUser = SecurityUtils.getLoginUser();
        SysLogs sysLogs = new SysLogs();
        sysLogs.setUsername(loginUser.getUsername());
        String className=joinPoint.getTarget().getClass().getName();
        String methodName=joinPoint.getSignature().getName();
        sysLogs.setMethod(className+methodName);
        Object[] args = joinPoint.getArgs();
        sysLogs.setParams(Arrays.toString(args));
        OperationType operationType = log.OPERATION_TYPE();
        sysLogs.setOperation(operationType.getType());
        sysLogs.setTime(System.currentTimeMillis());
        if (e!=null){
            sysLogs.setOperation(OperationType.FAIL.getType());
        }
        String ipAddr = IpUtils.getIpAddr(ServletUtils.getRequest());
        sysLogs.setIp(ipAddr);
        sysLogsMapper.insert(sysLogs);*/

        AsyncManager.getAsyncManager().execute(new AsyncFactory().sysLogsSaveTask(joinPoint,log,sysLogsMapper::insert,e));
    }
}
