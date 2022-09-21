package com.example.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.xml.internal.ws.developer.Serialization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统用户
 * @TableName sys_users
 */
@Data
@ToString
@EqualsAndHashCode
public class SysUsers implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */

    private String password;



    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态  0：禁用   1：正常  默认值 ：1
     */
    private Integer valid;

    /**
     * 
     */
    private Integer deptid;

    /**
     * 创建时间
     */
    private Date createdtime;

    /**
     * 修改时间
     */
    private Date modifiedtime;

    /**
     * 创建用户
     */
    private String createduser;

    /**
     * 修改用户
     */
    private String modifieduser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;



}