package com.medical.appointmentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    /**
     * Executor dùng Virtual Threads (Java 21).
     * Resilience4j TimeLimiter cần ExecutorService bean để thực thi CompletableFuture async.
     * Virtual Threads giúp không block platform thread khi chờ.
     */
    @Bean
    public ExecutorService virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}