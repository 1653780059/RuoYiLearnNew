package com.example.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 16537
 * @Classname LoginStates
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/30 10:57
 */
@AllArgsConstructor
@Getter
public enum LoginStates {
    /**
     * 登录成功
     */
    SUCCESS("1"),
    /**
     * 登录失败
     */
    FAIL("0");
    private String type;

}
