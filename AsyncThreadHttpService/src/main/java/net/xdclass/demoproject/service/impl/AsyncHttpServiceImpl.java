package net.xdclass.demoproject.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.xdclass.demoproject.service.AsyncHttpService;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncHttpServiceImpl implements AsyncHttpService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Async("taskExecutor")
    public CompletableFuture<String> sendHttpRequest(String url) {
        logger.info("Sending HTTP request to: {}", url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        logger.info("Received response from: {}", url);
        logger.info("Received response body from: {}",response.getBody());
        return CompletableFuture.completedFuture(response.getBody());
    }
}
