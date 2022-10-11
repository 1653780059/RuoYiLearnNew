package com.example.system.service;

import com.example.common.result.Result;

import java.io.IOException;

/**
 * @author 16537
 * @Classname WxPayService
 * @Description
 * @Version 1.0.0
 * @Date 2022/10/11 14:05
 */
public interface WxPayService {
    Result nativePay(String productId, String count) throws IOException;
}
