package com.example.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Classname LoginStates
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/20 13:31
 * @Created by 16537
 */
@Getter
@AllArgsConstructor
public enum AccountStates {
    /**
     * 账户状态正常
     */
    ON(1),
    /**
     * 账户删除
     */
    OFF(-1),
    /**
     * 账户封禁
     */
    DISABLE(0);
    private Integer type;
}
