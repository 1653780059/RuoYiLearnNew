package com.example.common.constants;

import javafx.beans.binding.Bindings;

/**
 * @Classname RedisConstants
 * @Description Redis相关常量
 * @Version 1.0.0
 * @Date 2022/9/19 22:06
 * @Created by 16537
 */
public final class RedisConstants {
    /**
     * 登录失败计数前缀
     */
    public static final String LOGIN_FAIL_PREFIX="login:fail:";
    /**
     * 登录失败次数
     */
    public static final Integer MAX_FAIL_COUNT=5;
    /**
     * 已登录用户前缀
     */
    public static final String LOGIN_USER_PREFIX="login:user:";
    /**
     * 验证码答案前缀
     */
    public static final String LOGIN_VERIFICATION_PREFIX="login:verification:";
    /**
     * 登录失败后等待时间10m
     */
    public static final Integer ALLOW_TRY_AGAIN_TIME=10;
    /**
     * 登录令牌有效期20m
     */
    public static final Integer LOGIN_EXPIRATION_TIME=20;
    /**
     * 验证码有效期5m
     */
    public static final Integer VERIFICATION_EXPIRATION_TIME = 5;
    /**
     * 防止缓存穿透的，无用户名前缀
     */
    public static final String USER_NOT_FOUND = "login:notfound:";
    /**
     * 无用户名有效期3m
     */
    public static final Integer USER_NOT_FOUND_EXPIRATION_TIME = 3;
    /**
     * 订单号生成前缀
     */
    public static final String ORDER_NO="order:no:";
    /**
     * 订单号生成锁
     */
    public static final String ORDER_NO_LOCK="order:no:lock";
    /**
     * 未支付订单二维码前缀
     */
    public static final String NO_PAY_ORDER="nopay:";
    /**
     * 微信支付订单处理锁前缀
     */
    public static final String NATIVE_PAY_NOTIFY_LOCK="native:pay:notify:lock:";
    /**
     * 支付通知锁失效时间20s
     */
    public static final Integer NATIVE_PAY_NOTIFY_LOCK_EXPIRATION= 20;
    /**
     * 未支付订单二维码失效时间20m
     */
    public static final int NO_PAY_ORDER_EXPIRATION = 20;
    public static final String NATIVE_REFUND_NOTIFY_LOCK = "native:refund:notify:lock:";
    public static final String REFUND_PROCESSING = "refund:processing:";
    /**
     * 退款通知锁失效时间20s
     */
    public static final Integer REFUND_PROCESSING_EXPIRATION = 20;
}
