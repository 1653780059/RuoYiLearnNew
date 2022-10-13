package com.example.admin;


import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.config.LearnConfig;
import com.example.common.holders.AuthenticationHolder;
import com.example.common.utils.JwtUtils;

import com.example.common.utils.MailUtils;
import com.example.common.utils.OrderNoUtils;
import com.example.dao.mapper.SysUsersMapper;
import com.example.farmwork.config.WxPayConfig;
import com.example.system.service.SysRefundInfoService;
import com.example.system.service.WxPayService;
import com.example.system.service.impl.WxPayServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

@SpringBootTest
class AdminApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SysUsersMapper usersMapper;
    @Autowired
    WxPayConfig wxPayConfig;
    @Autowired
    WxPayService wxPayService;
    @Autowired
    LearnConfig learnConfig;
    @Autowired
    SysRefundInfoService refundInfoService;

    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("1234"));
    }
    @Test
    void testSysUsersMapper(){
        System.out.println(usersMapper.getPermission(1));
    }
    @Test
    void testAuthenticationHolder(){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("user","123");
        AuthenticationHolder.setToken(usernamePasswordAuthenticationToken);
        System.out.println(AuthenticationHolder.getPassword());
    }
    @Test
    void testJWT(){
        System.out.println(JwtUtils.getToken("123"));
        System.out.println(JwtUtils.parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbiI6IjEyMyJ9.OGVf7WFueztOv1kchEIjpOYLmnU5RgyK1adimeGBlg8"));

    }
    @Test
    void testProperties(){
        Properties properties = new Properties();
        File file = new File("mail.properties");
        try {
            properties.load(new FileInputStream(file));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(properties.getProperty("mail.host"));
    }

    @Test
    void testMail(){
        //AsyncManager.getAsyncManager().execute(new AsyncFactory().sendMail("test","async测试","1653780059@qq.com"));
        MailUtils mailUtils = SpringUtil.getBean("mailUtils");
        mailUtils.sendEmail("test","async测试","1653780059@qq.com");
    }
    @Test
    void testWxPayConfig() throws MalformedURLException {
        System.out.println(learnConfig.getOrderTimeout());
    }
    @Test
    void testOrderNoUtil(){
        System.out.println(OrderNoUtils.getOrderNo());
    }
    @Test
    void testQueryOrder() throws IOException {
        System.out.println(wxPayService.queryOrder("ORDER_109152970100768772"));
    }
    @Test
    void testCloseOrder() throws Exception{
        wxPayService.closeOrder("ORDER_109154088832008203");
    }
    @Test
    void testHutool(){
        String s = "{\"mchid\":\"1558950191\",\"appid\":\"wx74862e0dfcf69954\",\"out_trade_no\":\"ORDER_109159349931409413\",\"transaction_id\":\"4200001541202210137364287356\",\"trade_type\":\"NATIVE\",\"trade_state\":\"SUCCESS\",\"trade_state_desc\":\"支付成功\",\"bank_type\":\"OTHERS\",\"attach\":\"1,1,1\",\"success_time\":\"2022-10-13T13:38:29+08:00\",\"payer\":{\"openid\":\"oHwsHuFKAKdchiCuXetYSkZFL3LY\"},\"amount\":{\"total\":1,\"payer_total\":1,\"currency\":\"CNY\",\"payer_currency\":\"CNY\"}}";
        String jsonStr = JSONUtil.toJsonStr(s);
        HashMap<String,Object> hashMap = JSONUtil.toBean(jsonStr, HashMap.class);
        String str = (String)hashMap.get("mchid");
        System.out.println(str);
    }
    @Test
    void testQueryRefund(){
        try {
            wxPayService.queryRefund("REFUND_109159596557598728");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void testGetBillUrl() throws IOException {
        System.out.println(wxPayService.getBill("2022-10-12", "tradebill"));
    }

}
