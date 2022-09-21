package com.example.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname Result
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/20 9:59
 * @Created by 16537
 */
@Data
public class Result {
    private Object data;
    private boolean status;
    private String msg;
    public Result ok(){
        status=true;
        return this;
    }
    public Result ok(Object data){
        this.status=true;
        this.data=data;
        return this;
    }
    public Result no(String msg){
        this.status=false;
        this.msg=msg;
        return this;
    }
    public Result no(Throwable throwable){
        this.status=false;
        this.msg=throwable.getMessage();
        return this;
    }
}
