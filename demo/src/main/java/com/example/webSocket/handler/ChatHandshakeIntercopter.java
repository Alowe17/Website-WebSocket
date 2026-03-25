package com.example.webSocket.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class ChatHandshakeIntercopter implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake (ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
                                    Map<String, Object> attributes) throws Exception {
        String path = request.getURI().getPath();
        String chatUuid = path.substring(path.lastIndexOf("/") + 1);

        attributes.put("chatUuid", chatUuid);
        return true;
    }

    @Override
    public void afterHandshake (ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception ex) {}
}