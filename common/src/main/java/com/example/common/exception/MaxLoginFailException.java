package com.example.common.exception;

/**
 * @Classname PasswordErrorException
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/20 10:08
 * @Created by 16537
 */
public class MaxLoginFailException extends RuntimeException{
    public MaxLoginFailException() {
        super();
    }

    public MaxLoginFailException(String message) {
        super(message);
    }

    public MaxLoginFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxLoginFailException(Throwable cause) {
        super(cause);
    }

    protected MaxLoginFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
