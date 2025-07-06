package com.example.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AsyncHttpService {
    private final RestTemplate restTemplate;

    public AsyncHttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public void sendAsyncResponse(String clientInfo) {
        try {
            String url = "http://localhost:8080/async/response";
            String response = restTemplate.postForObject(url, clientInfo, String.class);
            System.out.printf("[%s] HTTP通知结果: %s%n",
                    Thread.currentThread().getName(),
                    response);
        } catch (Exception e) {
            System.err.printf("[%s] HTTP通知失败: %s%n",
                    Thread.currentThread().getName(),
                    e.getMessage());
        }
    }
}