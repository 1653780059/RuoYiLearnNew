package com.example.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @TableName sys_login_info
 */
@TableName(value ="sys_login_info")
@Data
@EqualsAndHashCode
@ToString
public class SysLoginInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String userame;


    /**
     * 
     */
    private Date logintime;

    /**
     * 
     */
    private String os;

    /**
     * 
     */
    private String ip;

    /**
     * 
     */
    private String ipaddr;

    /**
     * 
     */
    private String browser;
    /**
     * 登录消息
     */
    private String msg;

    /**
     * 用户登录状态，1登录成功，0登录失败
     */
    private String state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}