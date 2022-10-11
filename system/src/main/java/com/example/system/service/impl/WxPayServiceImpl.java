package com.example.system.service.impl;

import cn.hutool.core.lang.hash.Hash;
import cn.hutool.json.JSONUtil;
import com.example.base.domain.SysOrderInfo;
import com.example.common.constants.RedisConstants;
import com.example.common.enums.OrderStatus;
import com.example.common.enums.wx.WxApiType;
import com.example.common.result.Result;
import com.example.farmwork.config.WxPayConfig;
import com.example.farmwork.factory.AsyncFactory;
import com.example.farmwork.factory.AsyncManager;
import com.example.farmwork.utils.SecurityUtils;
import com.example.farmwork.utils.WxPayUtils;
import com.example.system.service.SysOrderInfoService;
import com.example.system.service.WxPayService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private StringRedisTemplate redisTemplate;
    @Override
    public Result nativePay(String productId, String count) throws IOException {
        String key= RedisConstants.NO_PAY_ORDER.concat(SecurityUtils.getLoginUser().getId().toString()+":").concat(productId+":").concat(count);
        String redisUrl = redisTemplate.opsForValue().get(key);
        if(redisUrl!=null){
            log.info("数据库中有相同订单未支付，使用redis中缓存的url===>"+redisUrl);
            return Result.success().put("codeUrl",redisUrl);
        }
        log.info("下单开始");
        SysOrderInfo orderInfo = orderInfoService.newOrderInfo(productId,count);
        log.info("订单生成===>"+orderInfo);
        String url = wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType());
        log.info("请求url===>"+url);
        HttpPost httpPost = WxPayUtils.getNativePayRequestBody(url,orderInfo,wxPayConfig);
        String bodyAsStr;
        try(CloseableHttpResponse response = wxPayClient.execute(httpPost)){
            bodyAsStr = EntityUtils.toString(response.getEntity());
            WxPayUtils.assertResponse(response.getStatusLine().getStatusCode(),bodyAsStr);
        }
        HashMap<String,Object> hashMap = JSONUtil.toBean(bodyAsStr, HashMap.class);

        redisTemplate.opsForValue().set(key,(String) hashMap.get("code_url"),20, TimeUnit.MINUTES);
        return Result.success().put("codeUrl",hashMap.get("code_url"));
    }
}
