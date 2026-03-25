package com.example.webSocket.config;

import com.example.webSocket.handler.AuthWebSocketHandler;
import com.example.webSocket.handler.ChatHandshakeIntercopter;
import com.example.webSocket.service.ChatService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatService chatService;

    public WebSocketConfig (ChatService chatService) {
        this.chatService = chatService;
    }
    @Override
    public void registerWebSocketHandlers (WebSocketHandlerRegistry registry) {
        registry.addHandler(authWebSocketHandler(), "/ws/chat/{chatUuid}")
                .addInterceptors(new ChatHandshakeIntercopter())
                .setAllowedOriginPatterns("*");
    }

    private AuthWebSocketHandler authWebSocketHandler() {
        return new AuthWebSocketHandler(chatService);
    }
}