package com.example.system.factory;

import com.example.common.domain.SysLogs;
import com.example.common.domain.SysUsers;
import com.example.common.enums.OperationType;
import com.example.common.utils.IpUtils;
import com.example.common.utils.ServletUtils;
import com.example.farmwork.utils.SecurityUtils;
import com.example.system.annotation.Log;
import com.example.system.mapper.SysLogsMapper;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.function.Function;

/**
 * @author 16537
 * @Classname LoggerFactory
 * @Description 日志工厂
 * @Version 1.0.0
 * @Date 2022/9/26 15:24
 */
public class AsyncFactory {

    public TimerTask sysLogsSaveTask(JoinPoint joinPoint, Log log, Function<SysLogs,Integer> function,Exception e){
        SysUsers loginUser = SecurityUtils.getLoginUser();
        final HttpServletRequest request = ServletUtils.getRequest();
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    SysLogs sysLogs = new SysLogs();
                    sysLogs.setUsername(loginUser.getUsername());
                    String className=joinPoint.getTarget().getClass().getName();
                    String methodName=joinPoint.getSignature().getName();
                    sysLogs.setMethod(className+methodName);
                    Object[] args = joinPoint.getArgs();
                    sysLogs.setParams(Arrays.toString(args));
                    OperationType operationType = log.OPERATION_TYPE();
                    sysLogs.setOperation(operationType.getType());
                    sysLogs.setCreatedtime(System.currentTimeMillis());
                    String discription = log.title();
                    sysLogs.setDiscription(discription);
                    if (e!=null){
                        sysLogs.setOperation(OperationType.FAIL.getType());
                    }
                    String ip = IpUtils.getIpAddr(request);
                    sysLogs.setIp(ip);
                    function.apply(sysLogs);
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        };
    }

}
