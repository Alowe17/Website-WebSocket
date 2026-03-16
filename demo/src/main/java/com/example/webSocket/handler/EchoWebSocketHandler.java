package com.example.webSocket.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EchoWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished (WebSocketSession session) throws Exception {
        System.out.println("Клиент отправил обращение в поддержку: " + session.getId());
        session.sendMessage(new TextMessage("Соединение установлено! Вы можете отправить свое обращение."));
    }

    @Override
    protected void handleTextMessage (WebSocketSession session, TextMessage message) throws Exception {
        String received = message.getPayload();
        System.out.println("Получено от клиента: " + session.getId() + ": " + received);

        String response = "Ответ сервера (" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "): " + received;

        session.sendMessage(new TextMessage(response));
    }

    @Override
    public void afterConnectionClosed (WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Клиент отключился: сессия " + session.getId() + ", причина: " + status);
    }

    @Override
    public void handleTransportError (WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Ошибка в WebSocket сессии " + session.getId() + ": " + exception.getMessage());
    }
}