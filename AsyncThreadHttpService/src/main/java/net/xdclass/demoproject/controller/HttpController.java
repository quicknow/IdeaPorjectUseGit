package net.xdclass.demoproject.controller;
import net.xdclass.demoproject.service.AsyncHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class HttpController {

    @Autowired
    private AsyncHttpService asyncHttpService;

    @GetMapping("/sendRequest")
    public String sendHttpRequest() {
        String url = "http://127.0.0.1:8089/api/v1/video/list";

        // 调用异步方法
        CompletableFuture<String> responseFuture = asyncHttpService.sendHttpRequest(url);
        // 在这里可以做其他事情
        // 返回响应
        return "同步返回：HTTP request sent, check the logs for details.";
    }
}
