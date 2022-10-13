package com.example.system.service.timertask;

import com.example.base.domain.SysOrderInfo;
import com.example.base.domain.SysRefundInfo;
import com.example.common.constants.RedisConstants;
import com.example.common.constants.WxHttpConstants;
import com.example.common.enums.OrderStatus;
import com.example.common.enums.wx.WxTradeState;
import com.example.farmwork.factory.AsyncFactory;
import com.example.farmwork.factory.AsyncManager;
import com.example.system.service.SysOrderInfoService;
import com.example.system.service.SysRefundInfoService;
import com.example.system.service.WxPayService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname WxTimerTask
 * @Description
 * @Version 1.0.0
 * @Date 2022/10/12 10:51
 */
@Component
@AllArgsConstructor
@Slf4j
public class WxTimerTask {
    private SysOrderInfoService orderInfoService;
    private WxPayService wxPayService;
    private StringRedisTemplate redisTemplate;
    private SysRefundInfoService refundInfoService;

    /**
     * 定时处理超时订单
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void handleTimeOutOrders() {
        log.info("定时处理超时订单start");
        List<SysOrderInfo> orderInfos = orderInfoService.getTimeOutOrders();
        orderInfos.stream().forEach(order -> {
            try {
                log.info("处理订单==》" + order.getOrderNo());
                String orderKey = RedisConstants.NO_PAY_ORDER.concat(order.getUserId() + ":")
                        .concat(order.getProductId() + ":")
                        .concat(String.valueOf(order.getCount()));

                if (redisTemplate.opsForValue().get(orderKey) != null) {
                    String orderLock = RedisConstants.NATIVE_PAY_NOTIFY_LOCK.concat(order.getOrderNo());
                    if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(orderLock, "lock", RedisConstants.NATIVE_PAY_NOTIFY_LOCK_EXPIRATION, TimeUnit.SECONDS))) {
                        try {
                            HashMap<String, Object> bodyMap = wxPayService.queryOrder(order.getOrderNo());
                            String tradeState = (String) bodyMap.get(WxHttpConstants.TRADE_STATE);
                            if (WxTradeState.SUCCESS.getType().equals(tradeState)) {
                                redisTemplate.delete(orderKey);
                                AsyncManager.getAsyncManager().execute(new AsyncFactory().changeOrderStatusAndSavePaymentInfo(bodyMap));
                            } else {
                                redisTemplate.delete(orderKey);
                                wxPayService.closeOrder(order.getOrderNo());
                                orderInfoService.changeOrderStatus(order.getOrderNo(),OrderStatus.CLOSED.getType());
                            }
                        } finally {
                            redisTemplate.delete(orderLock);
                        }

                    }
                }

                log.info("处理完毕==》" + order.getOrderNo());
            } catch (IOException e) {
                e.printStackTrace();
                log.info("处理完毕==》" + order.getOrderNo());
                log.info("定时处理超时订单end");
                throw new RuntimeException(e);
            }


        });
        log.info("定时处理超时订单end;");

    }
    /**
     * 定时查询退款状态
     */
    @Scheduled(cron = "10/30 * * * * ?")
    public void handleRefundProcessing(){
        log.info("定时退款单处理start");
       List<SysRefundInfo> refundInfos = refundInfoService.getRefundProcessing();
       refundInfos.stream().forEach(refund->{
           final String refundLock = RedisConstants.NATIVE_REFUND_NOTIFY_LOCK.concat(refund.getRefundNo());
           final String refundKey = RedisConstants.REFUND_PROCESSING.concat(refund.getRefundNo());
           try {
               if(redisTemplate.opsForValue().setIfAbsent(refundLock,"lock",RedisConstants.REFUND_PROCESSING_EXPIRATION,TimeUnit.SECONDS)!=null){
                   try {
                       if(redisTemplate.opsForValue().get(refundKey)!=null){
                           log.info("处理退款单===>" + refund.getRefundNo());
                           final HashMap<String,Object> bodyMap = wxPayService.queryRefund(refund.getRefundNo());
                           AsyncManager.getAsyncManager().execute(new AsyncFactory().changeOrderAndRefundStatus(bodyMap));
                       }
                   }finally {
                       redisTemplate.delete(refundLock);
                   }
               }

           }catch (Exception e){
               e.printStackTrace();
               log.info("定时退款单处理end");

               throw new RuntimeException(e);
           }

       });
        log.info("定时退款单处理end");

    }
}
