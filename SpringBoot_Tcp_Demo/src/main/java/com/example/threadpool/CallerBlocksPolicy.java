package com.example.threadpool;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallerBlocksPolicy implements RejectedExecutionHandler {
    private final long timeout;
    private final TimeUnit unit;

    public CallerBlocksPolicy(long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            // 尝试将任务重新放入队列（最多等待指定时间）
            if (!executor.getQueue().offer(r, timeout, unit)) {
                throw new RejectedExecutionException("Task " + r.toString() +
                        " rejected from " + executor.toString());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException("Interrupted while offering task", e);
        }
    }
}