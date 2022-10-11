package com.example.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.example.common.constants.RedisConstants;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 订单号工具类
 *
 * @author qy
 * @since 1.0
 */
public class OrderNoUtils {
    /**
     * 获取订单编号
     * @return
     */
    public static String getOrderNo() {
        return "ORDER_" + getNo();
    }

    /**
     * 获取退款单编号
     * @return
     */
    public static String getRefundNo() {
        return "REFUND_" + getNo();
    }
    public static final Object LOCK = new Object();
    /**
     * 获取编号
     * @return
     */
    public static String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        Date date = new Date();
        String format = sdf.format(date);
        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        String key = RedisConstants.ORDER_NO.concat(format);
        String count;
        long no;
        while (!Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(RedisConstants.ORDER_NO_LOCK, "lock"))){
            synchronized (LOCK){
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if((count=redisTemplate.opsForValue().get(key))==null){
            redisTemplate.opsForValue().set(key,"0");
            count="0";
        }

        long time = date.getTime();
        no=time<<16 | Long.parseLong(count);
        int newCount = Integer.parseInt(count) + 1;
        redisTemplate.opsForValue().set(key,String.valueOf(newCount));
        synchronized (LOCK){
            redisTemplate.delete(RedisConstants.ORDER_NO_LOCK);
            LOCK.notifyAll();
        }
        return String.valueOf(no);

    }

}
