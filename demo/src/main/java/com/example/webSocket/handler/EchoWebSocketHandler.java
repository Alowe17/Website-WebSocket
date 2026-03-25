package com.example.webSocket.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EchoWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    @Override
    public void afterConnectionEstablished (WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Клиент отправил обращение в поддержку: " + session.getId());
        session.sendMessage(new TextMessage("Соединение установлено! Вы можете отправить свое обращение."));
    }

    @Override
    protected void handleTextMessage (WebSocketSession session, TextMessage message) throws Exception {
        String received = message.getPayload();
        System.out.println("Получено от клиента: " + session.getId() + ": " + received);

        String response = "Ответ сервера (" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "): " + received;
        broadcast("Клиент " + session.getId() + ": " + received);
    }

    @Override
    public void afterConnectionClosed (WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Клиент отключился: сессия " + session.getId() + ", причина: " + status);
    }

    @Override
    public void handleTransportError (WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Ошибка в WebSocket сессии " + session.getId() + ": " + exception.getMessage());
    }

    private void broadcast (String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            }

            catch (Exception e) {
                System.out.println("Ошибка отправки сообщения клиенту: " + session.getId());
            }
        }
    }
}