package com.example.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.base.domain.SysOrderInfo;
import com.example.base.domain.SysRefundInfo;
import com.example.common.constants.*;
import com.example.common.enums.OrderStatus;
import com.example.common.enums.wx.WxApiType;
import com.example.common.result.Result;
import com.example.farmwork.config.WxPayConfig;
import com.example.farmwork.factory.AsyncFactory;
import com.example.farmwork.factory.AsyncManager;
import com.example.farmwork.utils.SecurityUtils;
import com.example.farmwork.utils.WxPayUtils;
import com.example.system.service.SysOrderInfoService;
import com.example.system.service.SysRefundInfoService;
import com.example.system.service.WxPayService;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname WxPayServiceImpl
 * @Description
 * @Version 1.0.0
 * @Date 2022/10/11 14:05
 */
@Service
@AllArgsConstructor
@Slf4j
public class WxPayServiceImpl implements WxPayService {
    private SysOrderInfoService orderInfoService;
    private WxPayConfig wxPayConfig;
    private CloseableHttpClient wxPayClient;
    private SysRefundInfoService refundInfoService;
    private StringRedisTemplate redisTemplate;
    private Verifier verifier;
    @Override
    public Result nativePay(Long productId, Integer count) throws IOException {
        String orderKey= RedisConstants.NO_PAY_ORDER
                .concat(SecurityUtils.getLoginUser().getId().toString()+":")
                .concat(productId+":").concat(String.valueOf(count));
        String redisUrl = redisTemplate.opsForValue().get(orderKey);
        if(redisUrl!=null){
            log.info("数据库中有相同订单未支付，使用redis中缓存的url===>"+redisUrl);
            return Result.success().put("codeUrl",redisUrl);
        }
        log.info("下单开始");
        SysOrderInfo orderInfo = orderInfoService.newOrderInfoAndSave(productId,count);
        log.info("订单生成===>"+orderInfo);
        String url = wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType());
        log.info("请求url===>"+url);
        HttpPost httpPost = WxPayUtils.getNativePayRequest(url,orderInfo,wxPayConfig,count,productId,SecurityUtils.getLoginUser().getId().toString());
        final String bodyAsStr;
        try(CloseableHttpResponse response = wxPayClient.execute(httpPost)){
            bodyAsStr = EntityUtils.toString(response.getEntity());
            WxPayUtils.assertResponse(response.getStatusLine().getStatusCode(),bodyAsStr);
        }
        HashMap<String,Object> hashMap = JSONUtil.toBean(bodyAsStr, HashMap.class);

        redisTemplate.opsForValue().set(orderKey,(String) hashMap.get(WxHttpConstants.CODE_URL),RedisConstants.NO_PAY_ORDER_EXPIRATION,TimeUnit.MINUTES);
        return Result.success().put("codeUrl",hashMap.get(WxHttpConstants.CODE_URL));
    }

    @Override
    public String nativePayNotify(HttpServletRequest request, HttpServletResponse response) throws ValidationException, ParseException {
        log.info("支付通知执行");
        String decryptText=WxPayUtils.getDecryptText(verifier,request,wxPayConfig);
        log.info("解密请求数据===》"+decryptText);
        HashMap<String,Object> bodyMap = JSONUtil.toBean(decryptText, HashMap.class);
        HashMap<String ,Object> resultMap = new HashMap<>();
        try{
            final String orderLock = RedisConstants.NATIVE_PAY_NOTIFY_LOCK
                    .concat((String) bodyMap.get(WxHttpConstants.OUT_TRADE_NO));
            if(Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(orderLock, "lock",RedisConstants.NATIVE_PAY_NOTIFY_LOCK_EXPIRATION,TimeUnit.SECONDS))){
                try{
                    String attach = (String) bodyMap.get(WxHttpConstants.ATTACH);
                    String[] split = attach.split(",");
                    String key= RedisConstants.NO_PAY_ORDER.concat(split[2]+":").concat(split[1]+":").concat(split[0]);
                    if(redisTemplate.opsForValue().get(key)==null){
                        response.setStatus(HttpStatusCodeConstants.SUCCESS_WITH_BODY);
                        resultMap.put("code","SUCCESS");
                        resultMap.put("message","成功");
                        return JSONUtil.toJsonStr(resultMap);
                    }

                    redisTemplate.delete(key);
                    //修改订单状态，保存支付信息
                    AsyncManager.getAsyncManager()
                            .execute(new AsyncFactory().changeOrderStatusAndSavePaymentInfo(bodyMap));
                    AsyncManager.getAsyncManager()
                            .execute(new AsyncFactory().sendMail("感谢购买","您购买的商品支付成功",split[2]));
                }finally {
                    redisTemplate.delete(orderLock);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setStatus(HttpStatusCodeConstants.NETWORK_ERROR);
            resultMap.put("code","FAIL");
            resultMap.put("message","系统错误");
            return JSONUtil.toJsonStr(resultMap);
        }

        response.setStatus(HttpStatusCodeConstants.SUCCESS_WITH_BODY);
        resultMap.put("code","SUCCESS");
        resultMap.put("message","成功");
        return JSONUtil.toJsonStr(resultMap);

    }
    @Override
    public HashMap<String,Object> queryOrder(String orderNo) throws IOException {
        String url = wxPayConfig.getDomain()
                .concat(String.format(WxApiType.ORDER_QUERY_BY_NO.getType(),orderNo))
                .concat("?mchid=")
                .concat(wxPayConfig.getMchId());
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(WxHttpConstants.ACCEPT_HEADER, ContentTypeConstants.APPLICATION_JSON);
        final String bodyAsString;
        try(CloseableHttpResponse res = wxPayClient.execute(httpGet)){
            bodyAsString = EntityUtils.toString(res.getEntity());
            WxPayUtils.assertResponse(res.getStatusLine().getStatusCode(),bodyAsString);
        };
        return JSONUtil.toBean(bodyAsString,HashMap.class);
    }

    @Override
    public void closeOrder(String orderNo) throws IOException {
        HttpPost httpPost =WxPayUtils.getCloseOrderRequest(orderNo,wxPayConfig);
        try(CloseableHttpResponse response = wxPayClient.execute(httpPost)){
            WxPayUtils.assertResponse(response.getStatusLine().getStatusCode(),"");
        };


    }

    @Override
    public void channelOrder(String orderNo) throws IOException {
        closeOrder(orderNo);
        orderInfoService.changeOrderStatus(orderNo,OrderStatus.CANCEL.getType());
    }
    @Override
    public Result refundOrderInfo(String orderNo, String reason) {
        refundInfoService.newRefundInfoServiceAndSave(orderNo,reason);
        orderInfoService.changeOrderStatus(orderNo,OrderStatus.REFUND_APPLICATION.getType());
        return Result.success("退款申请成功");
    }

    @Override
    public Result refundConfirm(String refundNo, Integer refund) throws IOException {
        final String refundKey = RedisConstants.REFUND_PROCESSING
                .concat(refundNo);
        redisTemplate.opsForValue().set(refundKey,"processing");
        SysRefundInfo refundInfo = refundInfoService
                .getOne(new LambdaQueryWrapper<SysRefundInfo>().eq(SysRefundInfo::getRefundNo, refundNo));
        if (refundInfo==null){
            return Result.error("没有这个退款单");
        }
        final String bodyAsString;
        HttpPost httpPost = WxPayUtils.getRefundRequest(refundInfo, wxPayConfig,refund);
        try(CloseableHttpResponse response = wxPayClient.execute(httpPost)){
            bodyAsString = EntityUtils.toString(response.getEntity());
            WxPayUtils.assertResponse(response.getStatusLine().getStatusCode(),bodyAsString);
            AsyncManager.getAsyncManager()
                    .execute(new AsyncFactory().updateRefundInfoAndOrderState(JSONUtil.toBean(bodyAsString,HashMap.class),bodyAsString));
        }
        return Result.success("退款中");
    }

    @Override
    public String refundNotify(HttpServletRequest request, HttpServletResponse response) throws ValidationException, ParseException {
        String decryptText = WxPayUtils.getDecryptText(verifier, request, wxPayConfig);
        HashMap<String ,Object> bodyMap = JSONUtil.toBean(decryptText, HashMap.class);
        HashMap<String,Object> resultMap=new HashMap<>();
        final String refundKey = RedisConstants.REFUND_PROCESSING
                .concat((String) bodyMap.get(WxHttpConstants.OUT_REFUND_NO));
        final String refundLock = RedisConstants.NATIVE_REFUND_NOTIFY_LOCK
                .concat((String) bodyMap.get(WxHttpConstants.OUT_REFUND_NO));
        try {
            if(redisTemplate.opsForValue().setIfAbsent(refundLock,"lock",RedisConstants.REFUND_PROCESSING_EXPIRATION,TimeUnit.SECONDS)!=null){
                try {
                    if(redisTemplate.opsForValue().get(refundKey)==null){
                        response.setStatus(200);
                        resultMap.put("code","SUCCESS");
                        resultMap.put("message","成功");
                        return JSONUtil.toJsonStr(resultMap);
                    }
                    redisTemplate.delete(refundKey);
                    AsyncManager.getAsyncManager().execute(new AsyncFactory().changeOrderAndRefundStatus(bodyMap));
                }finally {
                    redisTemplate.delete(refundLock);
                }
            }

        }catch (Exception e){
            response.setStatus(500);
            resultMap.put("code","FAIL");
            resultMap.put("message","系统错误");
            return JSONUtil.toJsonStr(resultMap);
        }

        response.setStatus(200);
        resultMap.put("code","SUCCESS");
        resultMap.put("message","成功");
        return JSONUtil.toJsonStr(resultMap);
    }

    @Override
    public HashMap<String, Object> queryRefund(String refundNo) throws IOException {
        String url = wxPayConfig.getDomain().concat(String.format(WxApiType.DOMESTIC_REFUNDS_QUERY.getType(),refundNo));
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(WxHttpConstants.ACCEPT_HEADER,ContentTypeConstants.APPLICATION_JSON);
        final String bodyAsString;
        try(CloseableHttpResponse response = wxPayClient.execute(httpGet)){
            bodyAsString=EntityUtils.toString(response.getEntity());
            WxPayUtils.assertResponse(response.getStatusLine().getStatusCode(),bodyAsString);
        }
        return JSONUtil.toBean(bodyAsString,HashMap.class);
    }

    @Override
    public Result getBill(String date, String type) throws IOException {
        log.info("获取账单start");
        final String billUrl = getBillUrl(date, type);
        long timestamp = System.currentTimeMillis();
        String nonceStr = RandomUtil.randomString(16);
        HttpGet httpGet = new HttpGet(billUrl);
        httpGet.setHeader(WxHttpConstants.ACCEPT_HEADER,ContentTypeConstants.APPLICATION_JSON);
        HashMap<String,Object> bodyMap;
        try {
            final String authorizationHeader = wxPayConfig.getAuthorizationHeader(timestamp,nonceStr,httpGet.getMethod(),billUrl,"");
            httpGet.setHeader(WxHttpConstants.AUTHORIZATION_HEADER,authorizationHeader);
            log.info("请求头===》"+ Arrays.toString(httpGet.getHeaders(WxHttpConstants.AUTHORIZATION_HEADER)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        final String bodyAsString;
        log.info("账单获取请求发送");
        final CloseableHttpClient client = HttpClients.createDefault();
        try(final CloseableHttpResponse response = client.execute(httpGet)){
            bodyAsString = EntityUtils.toString(response.getEntity());
            WxPayUtils.assertResponse(response.getStatusLine().getStatusCode(),bodyAsString);
        }

        return Result.success(bodyAsString);
    }
    public String getBillUrl(String date,String type) throws IOException {
        String url;
        if ("tradebill".equals(type)){
            url=wxPayConfig.getDomain().concat(WxApiType.TRADE_BILLS.getType()).concat("?bill_date=").concat(date);
        }else if ("fundflowbill".equals(type)){
            url=wxPayConfig.getDomain().concat(WxApiType.FUND_FLOW_BILLS.getType()).concat("?bill_date=").concat(date);
        }else {
            throw new RuntimeException("没有对应账单类型");
        }
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(WxHttpConstants.ACCEPT_HEADER,ContentTypeConstants.APPLICATION_JSON);
        HashMap<String,Object> bodyMap;

        try(final CloseableHttpResponse response = wxPayClient.execute(httpGet) ) {
            final String bodyAsString = EntityUtils.toString(response.getEntity());
            WxPayUtils.assertResponse(response.getStatusLine().getStatusCode(),bodyAsString);
            bodyMap = JSONUtil.toBean(bodyAsString,HashMap.class);
        }
        return (String) bodyMap.get(WxHttpConstants.DOWNLOAD_URL);
    }
}
