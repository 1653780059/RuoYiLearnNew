package com.example.common.constants;

/**
 * @Classname RedisConstants
 * @Description Redis相关常量
 * @Version 1.0.0
 * @Date 2022/9/19 22:06
 * @Created by 16537
 */
public final class RedisConstants {
    /**
     * 登录失败前缀
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
     * 验证码结果前缀
     */
    public static final String LOGIN_VERIFICATION_PREFIX="login:verification:";
    /**
     * 登录失败后等待时间
     */
    public static final Integer ALLOW_TRY_AGAIN_TIME=10;
    /**
     * 登录令牌有效期
     */
    public static final Integer LOGIN_EXPIRATION_TIME=20;
    /**
     * 验证码有效期
     */
    public static final Integer VERIFICATION_EXPIRATION_TIME = 5;
    /**
     * 防止缓存穿透的，无用户名前缀
     */
    public static final String USER_NOT_FOUND = "login:notfound:";
    /**
     * 无用户名有效期
     */
    public static final Integer USER_NOT_FOUND_EXPIRATION_TIME = 3;
}
