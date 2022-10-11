package com.example.farmwork.utils;

import cn.hutool.json.JSONUtil;
import com.example.base.domain.SysOrderInfo;
import com.example.common.constants.ContentTypeConstants;
import com.example.common.constants.HttpStatusCodeConstants;
import com.example.common.constants.WxRequestConstants;
import com.example.common.enums.wx.WxNotifyType;
import com.example.farmwork.config.WxPayConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 16537
 * @Classname WxPayUtils
 * @Description
 * @Version 1.0.0
 * @Date 2022/10/11 14:30
 */
public class WxPayUtils {
    public static final Logger log = LoggerFactory.getLogger(WxPayUtils.class);
    /**
     * native下单请求体组装
     * @param url 请求url
     * @param orderInfo 订单信息
     * @param wxPayConfig  微信支付配置信息
     * @return
     */
    public static HttpPost getNativePayRequestBody(String url, SysOrderInfo orderInfo, WxPayConfig wxPayConfig) {
        HttpPost httpPost = new HttpPost(url);
        HashMap<String,Object> nativePayRequestMap = new HashMap<>();
        nativePayRequestMap.put(WxRequestConstants.APPID,wxPayConfig.getAppid());
        nativePayRequestMap.put(WxRequestConstants.MCHID,wxPayConfig.getMchId());
        nativePayRequestMap.put(WxRequestConstants.DESCRIPTION,orderInfo.getTitle());
        nativePayRequestMap.put(WxRequestConstants.OUT_TRADE_NO,orderInfo.getOrderNo());
        nativePayRequestMap.put(WxRequestConstants.NOTIFY_URL,wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        HashMap<String,Object> amount = new HashMap<>();
        amount.put(WxRequestConstants.TOTAL,orderInfo.getTotalFee());
        nativePayRequestMap.put(WxRequestConstants.AMOUNT,amount);
        String jsonStr = JSONUtil.toJsonStr(nativePayRequestMap);
        StringEntity stringEntity = new StringEntity(jsonStr,"utf-8");
        stringEntity.setContentType(ContentTypeConstants.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        httpPost.setHeader(WxRequestConstants.Accept_Header,ContentTypeConstants.APPLICATION_JSON);
        return httpPost;
    }

    public static void assertResponse(int statusCode, String bodyAsStr) throws IOException {
        if(statusCode== HttpStatusCodeConstants.SUCCESS_WITH_BODY){
            log.info("成功，返回结果===》"+bodyAsStr);
        }else if (statusCode==HttpStatusCodeConstants.SUCCESS_NO_BODY){
            log.info("成功，无返回值");
        }else {
            log.info("失败，状态码==》"+ statusCode+"\n返回结果===》"+bodyAsStr);
            throw new IOException();
        }
    }
}
