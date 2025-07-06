package com.example.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/async")
public class AsyncController {

    @PostMapping("/response")
    public String handleAsyncResponse(@RequestBody String clientInfo) {
        System.out.println("收到异步通知: " + clientInfo);
        return "hello";
    }
}