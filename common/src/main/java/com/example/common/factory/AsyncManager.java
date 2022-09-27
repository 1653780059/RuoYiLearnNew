package com.example.common.factory;

import cn.hutool.extra.spring.SpringUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname LoggerFactory
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 15:26
 */
@Slf4j
public class AsyncManager {
    private static final AsyncManager ASYNC_MANAGER = new AsyncManager();
    private static final ScheduledExecutorService executorService = SpringUtil.getBean("scheduledExecutorService");
    /**
     * 120秒
     */
    private static final Integer SHUTDOWN_TIME_OUT = 120;
    /**
     * 任务
     * 延迟时间
     * 10毫秒
     */
    private static final Integer TASK_TIME =10;
    private AsyncManager() {
    }

    ;

    public static AsyncManager getAsyncManager() {
        return ASYNC_MANAGER;
    }

    public void execute(TimerTask task) {
        executorService.schedule(task,TASK_TIME,TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(SHUTDOWN_TIME_OUT, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    if (!executorService.awaitTermination(SHUTDOWN_TIME_OUT, TimeUnit.SECONDS)) {
                        log.info("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

}
