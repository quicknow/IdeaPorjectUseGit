package com.example.service;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class TcpShortConnectionServer {
    private final int port;
    private final ThreadPoolExecutor tcpThreadPool;
    private final AsyncHttpService asyncHttpService;
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public TcpShortConnectionServer(
            @Value("${tcp.port:30024}") int port,
            @Qualifier("tcpThreadPool") ThreadPoolExecutor tcpThreadPool,
            @Lazy AsyncHttpService asyncHttpService
    ) {
        this.port = port;
        this.tcpThreadPool = tcpThreadPool;
        this.asyncHttpService = asyncHttpService;
    }

    public void start() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);

                while (running && !serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    handleIncomingConnection(clientSocket);
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("TCP Server error: " + e.getMessage());
                }
            } finally {
                gracefulShutdown();
            }
        }, "TCP-Main-Thread").start();
    }



    private void handleIncomingConnection(Socket clientSocket) {
        tcpThreadPool.execute(() -> {
            try (Socket socket = clientSocket;
                 InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream()) {

                processClientRequest(socket, input, output);

            } catch (IOException e) {
                handleClientError(clientSocket, e);
            }
        });
    }

    private void processClientRequest(Socket socket, InputStream input, OutputStream output)
            throws IOException {
        socket.setSoTimeout(5000); // 5秒读取超时

        String request = readRequest(input);
        if (request == null) return;

        String response = generateResponse(request);
        sendResponse(output, response);

        triggerAsyncNotification(socket, request);
    }

    private String readRequest(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8));
        String request = reader.readLine();

        if (request == null || request.trim().isEmpty()) {
            System.out.println("Received empty request");
            return null;
        }
        return request;
    }

    private String generateResponse(String request) {
        return "Server Response: " + request.toUpperCase();
    }

    private void sendResponse(OutputStream output, String response) throws IOException {
        output.write((response + "\n").getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    private void triggerAsyncNotification(Socket socket, String request) {
        String clientInfo = String.format("%s|%s",
                socket.getRemoteSocketAddress(),
                request.substring(0, Math.min(request.length(), 50)));

        try {
            asyncHttpService.sendAsyncResponse(clientInfo);
        } catch (Exception e) {
            System.err.println("Async notification failed: " + e.getMessage());
        }
    }

    private void handleClientError(Socket socket, Exception e) {
        String clientAddress = "unknown";
        try {
            clientAddress = socket.getRemoteSocketAddress().toString();
        } catch (Exception ignored) {}

        System.err.printf("Error handling client %s: %s%n",
                clientAddress, e.getMessage());
    }

    public synchronized void stop() {
        running = false;
        gracefulShutdown();
    }

    private void gracefulShutdown() {
        System.out.println("Initiating graceful shutdown...");

        closeServerSocket();
        shutdownThreadPool();
    }

    private void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }

    private void shutdownThreadPool() {
        tcpThreadPool.shutdown();
        try {
            if (!tcpThreadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                tcpThreadPool.shutdownNow();
                if (!tcpThreadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                    System.err.println("Thread pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            tcpThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}