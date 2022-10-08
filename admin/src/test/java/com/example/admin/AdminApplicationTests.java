package com.example.admin;


import cn.hutool.extra.spring.SpringUtil;
import com.example.common.holders.AuthenticationHolder;
import com.example.common.utils.JwtUtils;

import com.example.common.utils.MailUtils;
import com.example.dao.mapper.SysUsersMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

@SpringBootTest
class AdminApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SysUsersMapper usersMapper;

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

}
