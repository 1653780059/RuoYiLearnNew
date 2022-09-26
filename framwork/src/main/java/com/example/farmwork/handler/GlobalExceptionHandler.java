package com.example.farmwork.handler;

import com.example.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Classname SpringBootExceptionHandler
 * @Description 全局异常处理
 * @Version 1.0.0
 * @Date 2022/9/20 14:03
 * @Created by 16537
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Result ExceptionHandler(Throwable e){
        return new Result().no(e);
    }
}
