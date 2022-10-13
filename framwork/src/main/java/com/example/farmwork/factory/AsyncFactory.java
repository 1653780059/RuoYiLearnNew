package com.example.farmwork.factory;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.useragent.*;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.base.domain.*;
import com.example.common.constants.WxHttpConstants;
import com.example.common.enums.OperationType;
import com.example.common.enums.OrderStatus;
import com.example.common.enums.PayType;
import com.example.common.enums.wx.WxRefundStatus;
import com.example.common.utils.IPAddressUtils;
import com.example.common.utils.IpUtils;
import com.example.common.utils.MailUtils;
import com.example.common.utils.ServletUtils;
import com.example.dao.mapper.*;
import com.example.farmwork.utils.SecurityUtils;
import com.example.common.annotation.Log;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
     *
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
     *
     * @param username 登录用户名
     * @param state    登录状态 "1" 为成功 ，"0" 为失败
     * @param msg      登录消息
     * @return
     */
    public TimerTask sysLoginInfoLogTask(String username, String state, String msg) {
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

                } catch (Throwable e) {
                    e.printStackTrace();
                }


            }
        };
    }

    public TimerTask sendMail(String subject, String context, String... userIds) {
        SysUsersMapper sysUsersMapper = SpringUtil.getBean("sysUsersMapper");
        MailUtils mailUtils = SpringUtil.getBean("mailUtils");
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    for (String userId : userIds) {
                        SysUsers sysUsers = sysUsersMapper.selectOne(new LambdaQueryWrapper<SysUsers>().eq(SysUsers::getId, userId).select(SysUsers::getEmail));
                        String email = sysUsers.getEmail();
                        mailUtils.sendEmail(subject, context, email);
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }
        };
    }

    public TimerTask sysOrderInfoSaveTask(SysOrderInfo orderInfo) {
        return new TimerTask() {
            @Override
            public void run() {
                SysOrderInfoMapper orderInfoMapper = SpringUtil.getBean("sysOrderInfoMapper");
                orderInfoMapper.insert(orderInfo);
            }
        };
    }

    public TimerTask changeOrderStatusAndSavePaymentInfo(HashMap<String, Object> bodyMap) {
        SysOrderInfoMapper sysOrderInfoMapper = SpringUtil.getBean("sysOrderInfoMapper");
        SysPaymentInfoMapper sysPaymentInfoMapper = SpringUtil.getBean("sysPaymentInfoMapper");
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    String orderNo = (String) bodyMap.get(WxHttpConstants.OUT_TRADE_NO);
                    LambdaQueryWrapper<SysOrderInfo> sysOrderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    sysOrderInfoLambdaQueryWrapper.eq(SysOrderInfo::getOrderNo, orderNo);
                    SysOrderInfo orderInfo = new SysOrderInfo();
                    orderInfo.setOrderStatus(OrderStatus.SUCCESS.getType());
                    sysOrderInfoMapper.update(orderInfo, sysOrderInfoLambdaQueryWrapper);

                    SysPaymentInfo paymentInfo = new SysPaymentInfo();
                    paymentInfo.setContent(JSONUtil.toJsonStr(bodyMap));
                    paymentInfo.setCreateTime(new Date());
                    paymentInfo.setPaymentType(PayType.WXPAY.getType());
                    paymentInfo.setTradeState((String) bodyMap.get(WxHttpConstants.TRADE_STATE));
                    paymentInfo.setOrderNo(orderNo);
                    Object amount = bodyMap.get(WxHttpConstants.AMOUNT);
                    String jsonStr = JSONUtil.toJsonStr(amount);
                    HashMap<String, Object> amountMap = JSONUtil.toBean(jsonStr, HashMap.class);
                    paymentInfo.setPayerTotal((Integer) amountMap.get(WxHttpConstants.PAYER_TOTAL));
                    paymentInfo.setTransactionId((String) bodyMap.get(WxHttpConstants.TRANSACTION_ID));
                    paymentInfo.setTradeType((String) bodyMap.get(WxHttpConstants.TRADE_TYPE));
                    paymentInfo.setUpdateTime(new Date());
                    sysPaymentInfoMapper.insert(paymentInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public TimerTask updateRefundInfoAndOrderState(HashMap<String,Object> bodyMap,String bodyAsString) {
        SysRefundInfoMapper refundInfoMapper = SpringUtil.getBean("sysRefundInfoMapper");
        SysOrderInfoMapper orderInfoMapper = SpringUtil.getBean("sysOrderInfoMapper");
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    SysOrderInfo orderInfo = new SysOrderInfo();
                    SysRefundInfo refundInfo = new SysRefundInfo();
                    String state = (String) bodyMap.get(WxHttpConstants.STATUS);
                    Object amountStr = bodyMap.get(WxHttpConstants.AMOUNT);
                    String jsonStr = JSONUtil.toJsonStr(amountStr);
                    HashMap<String, Object> amount = JSONUtil.toBean(jsonStr, HashMap.class);
                    refundInfo.setRefund(Integer.valueOf(String.valueOf(amount.get(WxHttpConstants.REFUND))));
                    refundInfo.setRefundStatus(state);
                    refundInfo.setRefundId((String) bodyMap.get(WxHttpConstants.REFUND_ID));
                    refundInfo.setContentReturn(bodyAsString);
                    if (state.equals(WxRefundStatus.PROCESSING.getType())) {
                        orderInfo.setOrderStatus(OrderStatus.REFUND_PROCESSING.getType());
                    } else if (state.equals(WxRefundStatus.SUCCESS.getType())) {
                        orderInfo.setOrderStatus(OrderStatus.REFUND_SUCCESS.getType());
                    } else if (state.equals(WxRefundStatus.CLOSED.getType())) {
                        orderInfo.setOrderStatus(OrderStatus.REFUND_CLOSE.getType());
                    } else {
                        orderInfo.setOrderStatus(OrderStatus.REFUND_ABNORMAL.getType());
                    }

                    orderInfoMapper.update(orderInfo, new LambdaQueryWrapper<SysOrderInfo>().eq(SysOrderInfo::getOrderNo, bodyMap.get(WxHttpConstants.OUT_TRADE_NO)));
                    refundInfoMapper.update(refundInfo, new LambdaQueryWrapper<SysRefundInfo>().eq(SysRefundInfo::getRefundNo, bodyMap.get(WxHttpConstants.OUT_REFUND_NO)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public TimerTask changeOrderAndRefundStatus(HashMap<String, Object> bodyMap) {
        SysRefundInfoMapper refundInfoMapper = SpringUtil.getBean("sysRefundInfoMapper");
        SysOrderInfoMapper orderInfoMapper = SpringUtil.getBean("sysOrderInfoMapper");
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    SysOrderInfo orderInfo = new SysOrderInfo();
                    SysRefundInfo refundInfo = new SysRefundInfo();
                    String state = (String) bodyMap.get(WxHttpConstants.REFUND_STATUS);
                    if(state==null){
                        state=(String) bodyMap.get(WxHttpConstants.STATUS);
                    }
                    if (state.equals(WxRefundStatus.SUCCESS.getType())) {
                        orderInfo.setOrderStatus(OrderStatus.REFUND_SUCCESS.getType());
                    } else if (state.equals(WxRefundStatus.CLOSED.getType())) {
                        orderInfo.setOrderStatus(OrderStatus.REFUND_CLOSE.getType());
                    } else {
                        orderInfo.setOrderStatus(OrderStatus.REFUND_ABNORMAL.getType());
                    }
                    refundInfo.setRefundStatus(state);
                    orderInfoMapper.update(orderInfo, new LambdaQueryWrapper<SysOrderInfo>().eq(SysOrderInfo::getOrderNo, bodyMap.get(WxHttpConstants.OUT_TRADE_NO)));
                    refundInfoMapper.update(refundInfo, new LambdaQueryWrapper<SysRefundInfo>().eq(SysRefundInfo::getRefundNo, bodyMap.get(WxHttpConstants.OUT_REFUND_NO)));
                }catch (Exception e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
