package io.renren.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时任务配置类
 * 
 * 配置定时任务的线程池和执行策略
 *
 * @author renren
 * @since 1.0.0
 */
@Configuration
@EnableAsync
public class ScheduleConfig {

    /**
     * 配置定时任务线程池
     * 
     * @return ThreadPoolTaskScheduler
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        
        // 设置线程池大小
        scheduler.setPoolSize(10);
        
        // 设置线程名前缀
        scheduler.setThreadNamePrefix("scheduled-task-");
        
        // 设置等待任务完成后再关闭线程池
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        
        // 设置等待时间
        scheduler.setAwaitTerminationSeconds(60);
        
        // 设置拒绝策略
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 设置错误处理
        scheduler.setErrorHandler(throwable -> {
            // 记录定时任务执行错误
            System.err.println("定时任务执行出错: " + throwable.getMessage());
            throwable.printStackTrace();
        });
        
        return scheduler;
    }
}
