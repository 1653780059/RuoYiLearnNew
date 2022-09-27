package com.example.system.annotation;

import com.example.common.enums.OperationType;

import java.lang.annotation.*;

/**
 * @author 16537
 * @Classname Log
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/27 11:25
 */
@Target(value = { ElementType.PARAMETER, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String title() default "";
    OperationType OPERATION_TYPE();

}
