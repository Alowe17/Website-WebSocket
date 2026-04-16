package com.example.webSocket.controller.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketController (SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/support")
    public void support(String message, Principal user) {
        String username = user.getName();

        simpMessagingTemplate.convertAndSendToUser(username, "/queue", "Получено: " + message);
    }
}