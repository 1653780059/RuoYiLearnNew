package com.example.common.constants;

/**
 * @Classname RedisConstants
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/19 22:06
 * @Created by 16537
 */
public final class RedisConstants {
    public static final String LOGIN_FAIL_PREFIX="login:fail:";
    public static final Integer MAX_FAIL_COUNT=5;
    public static final String LOGIN_USER_PREFIX="login:user:";
    public static final String LOGIN_VERIFICATION_PREFIX="login:verification:";
    /**
     * 登录失败后等待时间
     */
    public static final Integer ALLOW_TRY_AGAIN_TIME=10;
    public static final Integer LOGIN_EXPIRATION_TIME=20;
    public static final Integer VERIFICATION_EXPIRATION_TIME = 5;
}
