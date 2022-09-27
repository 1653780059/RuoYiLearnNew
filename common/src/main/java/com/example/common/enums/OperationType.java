package com.example.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 16537
 * @Classname OperationType
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/27 11:28
 */
@AllArgsConstructor
@Getter
public enum OperationType {
    /**
     * 更新操作
     */
    UPDATE("更新"),
    /**
     * 删除操作
     */
    DELETE("删除"),
    /**
     * 查找操作
     */
    SELECT("查找"),
    /**
     * 新增操作
     */
    INSERT("新增"),
    /**
     * 操作失败
     */
    FAIL("失败");

    private String type;
}
