package com.example.farmwork.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

/**
 * @author 16537
 * @Classname WxPayConfig
 * @Description
 * @Version 1.0.0
 * @Date 2022/10/11 8:57
 */
@Component

@ConfigurationProperties(prefix = "wxpay")
@Data
public class WxPayConfig {
    public String mchId;
    private String mchSerialNo;
    private String privateKeyPath;
    private String apiV3Key;
    private String appid;
    private String domain;
    private String notifyDomain;

    public PrivateKey getPrivateKey(){
        PrivateKey privateKey;
        try {
            privateKey = PemUtil.loadPrivateKey(new FileInputStream("apiclient_key.pem"));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        return privateKey;
    }
    @Bean
    public Verifier verifier(){
        PrivateKey privateKey = getPrivateKey();
        PrivateKeySigner signer = new PrivateKeySigner(mchSerialNo, privateKey);
        WechatPay2Credentials credentials = new WechatPay2Credentials(mchId, signer);
        CertificatesManager instance = CertificatesManager.getInstance();
        Verifier verifier;
        try {
            instance.putMerchant(mchId,credentials,apiV3Key.getBytes(StandardCharsets.UTF_8));
            verifier = instance.getVerifier(mchId);
        } catch (IOException | GeneralSecurityException | HttpCodeException |NotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return verifier;

    }
    @Bean
    public CloseableHttpClient closeableHttpClient(Verifier verifier){
        PrivateKey privateKey = getPrivateKey();
        WechatPayHttpClientBuilder wechatPayHttpClientBuilder = WechatPayHttpClientBuilder.create();
        wechatPayHttpClientBuilder.withMerchant(mchId,mchSerialNo,privateKey)
                .withValidator(new WechatPay2Validator(verifier));
        return wechatPayHttpClientBuilder.build();
    }

    /**
     *  手动签名
     *
     * @param timestamp 时间戳
     * @param nonceStr  随机字符串
     * @param method    请求方式
     * @param url       请求url
     * @param body      请求体 请求体为空则传入空串
     * @return  认证字符串
     * @throws Exception 异常
     */
    public String getAuthorizationHeader(long timestamp, String nonceStr, String method, String url,String body) throws Exception {
        URL url1 = new URL(url);
        String uri = url1.getPath();
        if(url1.getQuery()!=null){
            uri=uri.concat("?").concat(url1.getQuery());
        }
        String signatureStr=getSignatureStr(timestamp,nonceStr,method,uri,body);
        PrivateKey privateKey = getPrivateKey();
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        byte[] signByte = sign.sign();
        String signature = Base64.getEncoder().encodeToString(signByte);
        return "WECHATPAY2-SHA256-RSA2048"+" "
                +"mchid="+"\""+mchId+"\""+","
                +"timestamp="+"\""+timestamp/1000+"\""+","
                +"nonce_str="+"\""+nonceStr+"\""+","
                +"serial_no="+"\""+mchSerialNo+"\""+","
                +"signature="+"\""+signature+"\"";
    }

    /**
     * 组装签名字符串
     * @param timestamp 时间戳
     * @param nonceStr  随机字符串
     * @param method    请求方式
     * @param uri       请求uri
     * @param body      请求体
     * @return 签名字符串
     */
    private String getSignatureStr(long timestamp, String nonceStr, String method, String uri, String body) {

        return method+"\n"
                +uri+"\n"
                +timestamp/1000+"\n"
                +nonceStr+"\n"
                +body+"\n";
    }


}
