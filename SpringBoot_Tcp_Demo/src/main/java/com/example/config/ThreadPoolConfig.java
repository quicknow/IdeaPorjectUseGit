package com.example.config;

import com.example.threadpool.CallerBlocksPolicy;
import com.example.threadpool.CustomThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Value("${tcp.pool.core-size:10}")
    private int corePoolSize;

    @Value("${tcp.pool.max-size:20}")
    private int maxPoolSize;

    @Value("${tcp.pool.queue-capacity:1000}")
    private int queueCapacity;

    @Value("${tcp.pool.keep-alive-seconds:60}")
    private int keepAliveSeconds;

    @Bean("tcpThreadPool")
    public ThreadPoolExecutor tcpThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new CustomThreadFactory("TCP-Worker-"),
                new CallerBlocksPolicy(30, TimeUnit.SECONDS) // 自定义拒绝策略
        );
    }
}