package com.example.system.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.lang.UUID;
import com.example.common.constants.RedisConstants;
import com.example.common.result.Result;
import com.example.common.utils.ServletUtils;
import com.example.system.service.VerificationGenerateService;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname VerificationGenerateServiceImpl
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/21 13:53

 */
@Service
public class VerificationGenerateServiceImpl implements VerificationGenerateService {
    @Autowired
    Producer producer;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 获取验证码，保存验证码结果到redis
     * @return 验证码图片流，验证码匹配的uuid
     */
    @Override
    public Result verificationGenerate() {
        Map<String,Object> map = new HashMap<>();
        String text = producer.createText();
        String[] split = text.split("@");
        String question = split[0];
        String answer = split[1];
        BufferedImage image = producer.createImage(question);
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image,"jpg",out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String uuid = UUID.randomUUID().toString();
        map.put("uuid", uuid);
        String key= RedisConstants.LOGIN_VERIFICATION_PREFIX+uuid;
        stringRedisTemplate.opsForValue().set(key,answer,RedisConstants.VERIFICATION_EXPIRATION_TIME, TimeUnit.MINUTES);
        map.put("img", Base64.encode(out.toByteArray()));
        return new Result().ok(map);
    }
}
