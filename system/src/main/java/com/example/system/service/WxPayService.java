package com.example.system.service;

import com.example.common.result.Result;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author 16537
 * @Classname WxPayService
 * @Description
 * @Version 1.0.0
 * @Date 2022/10/11 14:05
 */
public interface WxPayService {
    /**
     * 微信native下单
     * @param productId 商品id
     * @param count 商品数量
     * @return 微信支付二维码链接code_url
     * @throws IOException IO异常
     */
    Result nativePay(Long productId, Integer count) throws IOException;

    /**
     * 微信native支付通知接口
     * @param request 请求对象
     * @param response 响应对象
     * @return 系统对支付通知的处理状态
     * @throws ValidationException 签名验证异常
     * @throws ParseException   数据解析异常
     */
    String nativePayNotify(HttpServletRequest request, HttpServletResponse response) throws ValidationException, ParseException;

    /**
     * 微信native查询订单状态
     * @param orderNo 订单编号
     * @return 返回查询结果转化的map
     * @throws IOException IO异常
     */
    HashMap<String,Object> queryOrder(String orderNo) throws IOException;

    /**
     * 微信native关闭订单
     * @param orderNo 订单号
     * @throws IOException IO异常
     */
    void closeOrder(String orderNo) throws IOException;

    /**
     * 微信native取消订单
     * @param orderNo 订单号
     * @throws IOException IO异常
     */
    void channelOrder(String orderNo) throws IOException;

    /**
     * 微信native退款信息组装
     * @param orderNo 订单编号
     * @param reason 退款原因
     * @return 信息提交msg
     */
    Result refundOrderInfo(String orderNo, String reason);

    /**
     * 微信native退款发起
     * @param refundNo 退款单号
     * @param refund 退款金额
     * @return 退款是否成功msg
     * @throws IOException IO异常
     */
    Result refundConfirm(String refundNo, Integer refund) throws IOException;

    /**
     * 退款通知处理
     * @param request 请求对象
     * @param response 响应对象
     * @return 处理结果
     * @throws ValidationException 验签异常
     * @throws ParseException 解析异常
     */
    String refundNotify(HttpServletRequest request, HttpServletResponse response) throws ValidationException, ParseException;

    /**
     * 查询退款中订单
     * @param refundNo 退款单号
     * @return 返回查询结果
     * @throws IOException IO异常
     */
    HashMap<String, Object> queryRefund(String refundNo) throws IOException;

    /**
     * 下载账单
     * @param date 日期
     * @param type 账单类型
     * @return 账单信息 保存在data中
     * @throws IOException IO异常
     */
    Result getBill(String date, String type) throws IOException;
}
