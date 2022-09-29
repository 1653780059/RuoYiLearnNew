package com.example.farmwork.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 16537
 * @Classname ScheduledExecutorConfig
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/27 11:08
 */
@Configuration
public class ThreadsPoolConfig {
    @Bean("scheduledExecutorService")
    public ScheduledExecutorService getScheduleExecutorService(){
        return new ScheduledThreadPoolExecutor(20,
                new ThreadFactoryBuilder().setNamePrefix("scheduled_pool_").setDaemon(true).build(),
                //拒绝策略，阻塞队列满了，则使用当前线程去执行
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
