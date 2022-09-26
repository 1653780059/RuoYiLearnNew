package com.example.farmwork.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Classname RedisConfig
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/19 16:15
 * @Created by 16537
 */
@Configuration
public class RedisConfig {
    @Autowired
    RedisConnectionFactory connectionFactory;
    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        FastJsonRedisSerializer objectFastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(objectFastJsonRedisSerializer);
        stringRedisTemplate.setHashValueSerializer(objectFastJsonRedisSerializer);
        stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return stringRedisTemplate;
    }
}
