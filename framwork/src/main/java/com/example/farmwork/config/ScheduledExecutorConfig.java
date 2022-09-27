package com.example.farmwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author 16537
 * @Classname ScheduledExecutorConfig
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/27 11:08
 */
@Configuration
public class ScheduledExecutorConfig {
    @Bean("scheduledExecutorService")
    public ScheduledExecutorService getScheduleExecutorService(){
        return new ScheduledThreadPoolExecutor(20);
    }
}
