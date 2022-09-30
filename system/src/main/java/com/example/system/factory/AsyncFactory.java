package com.example.system.factory;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.useragent.*;
import com.example.common.domain.SysLoginInfo;
import com.example.common.domain.SysLogs;
import com.example.common.domain.SysUsers;
import com.example.common.enums.OperationType;
import com.example.common.utils.IPAddressUtils;
import com.example.common.utils.IpUtils;
import com.example.common.utils.ServletUtils;
import com.example.farmwork.utils.SecurityUtils;
import com.example.system.annotation.Log;
import com.example.system.mapper.SysLoginInfoMapper;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
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
    /**
     * Aop 切面 异步持久化日志
     * @param joinPoint
     * @param log
     * @param function
     * @param e
     * @return
     */
    public TimerTask sysLogsSaveTask(JoinPoint joinPoint, Log log, Function<SysLogs, Integer> function, Exception e) {
        SysUsers loginUser = SecurityUtils.getLoginUser();
        final HttpServletRequest request = ServletUtils.getRequest();
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    SysLogs sysLogs = new SysLogs();
                    sysLogs.setUsername(loginUser.getUsername());
                    String className = joinPoint.getTarget().getClass().getName();
                    String methodName = joinPoint.getSignature().getName();
                    sysLogs.setMethod(className + methodName);
                    Object[] args = joinPoint.getArgs();
                    sysLogs.setParams(Arrays.toString(args));
                    OperationType operationType = log.OPERATION_TYPE();
                    sysLogs.setOperation(operationType.getType());
                    sysLogs.setCreatedtime(System.currentTimeMillis());
                    String discription = log.title();
                    sysLogs.setDiscription(discription);
                    if (e != null) {
                        sysLogs.setOperation(OperationType.FAIL.getType());
                    }
                    String ip = IpUtils.getIpAddr(request);
                    sysLogs.setIp(ip);
                    function.apply(sysLogs);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 登录信息
     * @param username 登录用户名
     * @param state 登录状态 "1" 为成功 ，"0" 为失败
     * @param msg  登录消息
     * @return
     */
    public TimerTask sysLoginInfoLogTask(String username,String state,String msg) {
        SysLoginInfoMapper sysLoginInfoMapper = SpringUtil.getBean("sysLoginInfoMapper");
        UserAgent userAgent = UserAgentUtil.parse(ServletUtils.getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    SysLoginInfo sysLoginInfo = new SysLoginInfo();
                    sysLoginInfo.setState(state);
                    sysLoginInfo.setMsg(msg);
                    String browser = userAgent.getBrowser().getName();
                    String os = userAgent.getOs().getName();
                    String addressByIP = IPAddressUtils.getAddressByIP(ip);
                    sysLoginInfo.setIp(ip);
                    sysLoginInfo.setBrowser(browser);
                    sysLoginInfo.setOs(os);
                    sysLoginInfo.setIpaddr(addressByIP);
                    sysLoginInfo.setUserame(username);
                    sysLoginInfo.setLogintime(new Date());
                    sysLoginInfoMapper.insert(sysLoginInfo);

                }catch (Throwable e){
                    e.printStackTrace();
                }


            }
        };
    }

}
