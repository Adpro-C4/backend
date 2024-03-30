package com.adpro.backend.modules.commonmodule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1800);
        executor.setMaxPoolSize(2000);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("ThreadPool-");
        executor.initialize();
        return executor;
    }
}

