package com.example.admin.controller;

import com.example.farmwork.utils.PageUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 16537
 * @Classname BaseContrller
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 8:55
 */
public class BaseController {
    /**
     * 绑定数据映射
     * @param webDataBinder
     */
    @InitBinder
    public void initBinderDate(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
        webDataBinder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),false));
    }

    /**
     * 分页设置
     */
    public void startPage(){
        PageUtils.startPage();
    }
}
