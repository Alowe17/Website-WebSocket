package com.example.webSocket.config;

import com.example.webSocket.handler.EchoWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers (WebSocketHandlerRegistry registry) {
        registry.addHandler(echoWebSocketHandler(), "/echo")
                .setAllowedOriginPatterns("*");
    }

    private EchoWebSocketHandler echoWebSocketHandler() {
        return new EchoWebSocketHandler();
    }
}