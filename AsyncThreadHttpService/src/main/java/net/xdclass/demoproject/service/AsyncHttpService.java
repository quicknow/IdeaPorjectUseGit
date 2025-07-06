package net.xdclass.demoproject.service;

import java.util.concurrent.CompletableFuture;

public interface AsyncHttpService {

    CompletableFuture<String> sendHttpRequest(String url);
}
