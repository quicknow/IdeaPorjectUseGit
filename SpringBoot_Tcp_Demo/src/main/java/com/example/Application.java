package com.example;


import com.example.service.TcpShortConnectionServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final TcpShortConnectionServer tcpServer;

    public Application(@Lazy TcpShortConnectionServer tcpServer) {
        this.tcpServer = tcpServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        tcpServer.start();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}