package com.example.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统日志
 * @TableName sys_logs
 */
@TableName(value ="sys_logs")
@Data
@EqualsAndHashCode
@ToString
public class SysLogs implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户操作
     */
    private String operation;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 执行时长(毫秒)
     */
    @TableField(value = "createdTime")
    private Long createdtime;

    /**
     * IP地址
     */
    private String ip;


    /**
     * ip对应物理地址
     */
    @TableField("ipAddr")
    private String ipaddr;
    /**
     * 操作表述
     */
    private String discription;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}