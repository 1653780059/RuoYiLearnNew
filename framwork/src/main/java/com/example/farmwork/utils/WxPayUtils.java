package com.example.farmwork.utils;

import cn.hutool.json.JSONUtil;
import com.example.base.domain.SysOrderInfo;
import com.example.base.domain.SysRefundInfo;
import com.example.common.constants.ContentTypeConstants;
import com.example.common.constants.HttpStatusCodeConstants;
import com.example.common.constants.WxHttpConstants;

import com.example.common.enums.wx.WxApiType;
import com.example.common.enums.wx.WxNotifyType;
import com.example.common.utils.HttpUtils;
import com.example.farmwork.config.WxPayConfig;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
     * @param count
     * @param productId
     * @param userId
     * @return
     */
    public static HttpPost getNativePayRequest(String url, SysOrderInfo orderInfo, WxPayConfig wxPayConfig, Integer count, Long productId, String userId) {
        HttpPost httpPost = new HttpPost(url);
        HashMap<String,Object> nativePayRequestMap = new HashMap<>();
        nativePayRequestMap.put(WxHttpConstants.APPID,wxPayConfig.getAppid());
        nativePayRequestMap.put(WxHttpConstants.MCHID,wxPayConfig.getMchId());
        nativePayRequestMap.put(WxHttpConstants.DESCRIPTION,orderInfo.getTitle());
        nativePayRequestMap.put(WxHttpConstants.OUT_TRADE_NO,orderInfo.getOrderNo());
        nativePayRequestMap.put(WxHttpConstants.NOTIFY_URL,wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        nativePayRequestMap.put(WxHttpConstants.ATTACH,String.valueOf(count).concat(",").concat(String.valueOf(productId)).concat(",").concat(userId));
        HashMap<String,Object> amount = new HashMap<>();
        amount.put(WxHttpConstants.TOTAL,orderInfo.getTotalFee());
        postRequestSetting(httpPost, nativePayRequestMap, amount);
        return httpPost;
    }

    public static void assertResponse(int statusCode, String bodyAsStr) throws IOException {
        if(statusCode== HttpStatusCodeConstants.SUCCESS_WITH_BODY){
            log.info("成功，返回结果===》"+bodyAsStr);
        }else if (statusCode==HttpStatusCodeConstants.SUCCESS_NO_BODY){
            log.info("成功，无返回值");
        }else {
            log.info("失败，状态码==》"+ statusCode+"\n返回结果===》"+bodyAsStr);
            throw new IOException(bodyAsStr);
        }
    }

    /**
     * 验签解密 获取加密数据
     * @param verifier 签名验证器
     * @param request   请求对象
     * @param wxPayConfig wxPay配置信息
     * @return 解密字符串
     * @throws ValidationException 验签失败
     * @throws ParseException 解析失败
     */
    public static String getDecryptText(Verifier verifier, HttpServletRequest request, WxPayConfig wxPayConfig) throws ValidationException, ParseException {
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String signature = request.getHeader("Wechatpay-Signature");
        String body = HttpUtils.readData(request);
        String nonce = request.getHeader("Wechatpay-Nonce");
        String wechatPaySerial=request.getHeader("Wechatpay-Serial");
        NotificationRequest notificationRequest = new NotificationRequest.Builder()
                .withSerialNumber(wechatPaySerial)
                .withBody(body)
                .withNonce(nonce)
                .withTimestamp(timestamp)
                .withSignature(signature)
                .build();
        NotificationHandler handler = new NotificationHandler(verifier,wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        Notification parse = handler.parse(notificationRequest);
        if(parse!=null){
            return parse.getDecryptData();
        }else {
            return "";
        }
    }

    public static HttpPost getCloseOrderRequest(String orderNo, WxPayConfig wxPayConfig) {
        String url = wxPayConfig.getDomain()
                .concat(String.format(WxApiType.CLOSE_ORDER_BY_NO.getType(),orderNo));
        HttpPost httpPost = new HttpPost(url);
        HashMap<String,Object> requestParam = new HashMap<>();
        requestParam.put(WxHttpConstants.MCHID,wxPayConfig.getMchId());
        postRequestSetting(httpPost,requestParam,null);
        return httpPost;
    }

    public static HttpPost getRefundRequest(SysRefundInfo refundInfo, WxPayConfig wxPayConfig, Integer refund) {
        String url = wxPayConfig.getDomain().concat(WxApiType.DOMESTIC_REFUNDS.getType());
        HttpPost httpPost =new HttpPost(url);
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put(WxHttpConstants.OUT_TRADE_NO,refundInfo.getOrderNo());
        requestParams.put(WxHttpConstants.OUT_REFUND_NO,refundInfo.getRefundNo());
        requestParams.put(WxHttpConstants.REASON,refundInfo.getReason());
        String notifyUrl = wxPayConfig.getNotifyDomain().concat(WxNotifyType.REFUND_NOTIFY.getType());
        requestParams.put(WxHttpConstants.NOTIFY_URL,notifyUrl);
        HashMap<String,Object> amount = new HashMap<>();
        amount.put(WxHttpConstants.REFUND,refund);
        amount.put(WxHttpConstants.TOTAL,refundInfo.getTotalFee());
        amount.put(WxHttpConstants.CURRENCY,"CNY");
        postRequestSetting(httpPost, requestParams, amount);

        return httpPost;
    }

    private static void postRequestSetting(HttpPost httpPost, HashMap<String, Object> requestParams, HashMap<String, Object> amount) {
        if(amount!=null){
            requestParams.put(WxHttpConstants.AMOUNT,amount);
        }
        String jsonStr = JSONUtil.toJsonStr(requestParams);
        StringEntity entity = new StringEntity(jsonStr,"utf-8");
        entity.setContentType(ContentTypeConstants.APPLICATION_JSON);
        httpPost.setEntity(entity);
        httpPost.setHeader(WxHttpConstants.ACCEPT_HEADER,ContentTypeConstants.APPLICATION_JSON);
    }
}
