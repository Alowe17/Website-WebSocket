package com.example.webSocket.handler;

import com.example.webSocket.service.ChatService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AuthWebSocketHandler extends TextWebSocketHandler {
    private final ChatService chatService;
    private final Map<String, Set<WebSocketSession>> chatSessions = new ConcurrentHashMap<>();

    public AuthWebSocketHandler (ChatService chatService) {
        this.chatService = chatService;
    }
    @Override
    public void afterConnectionEstablished (WebSocketSession session) throws Exception {
        Principal principal = session.getPrincipal();

        if (principal == null) {
            System.out.println("Пользователь не аутентифицирован. Закрываем соединение.");
            session.close();
            return;
        }

        String uuid = (String) session.getAttributes().get("chatUuid");

        if (!chatService.userHasAccess(uuid, principal.getName())) {
            System.out.println("Пользователь " + principal.getName() + " не имеет доступа к чату с UUID: " + uuid + ". Закрываем соединение.");
            session.close();
            return;
        }

        chatSessions.computeIfAbsent(uuid, key -> ConcurrentHashMap.newKeySet()).add(session);
        System.out.println("Пользователь " + principal.getName() + " подключился к чату с UUID: " + uuid);

        String username = principal.getName();
        System.out.println("Пользователь " + username + " подключился к WebSocket.");
        session.sendMessage(new TextMessage("Привет, " + username + "! Вы успешно подключились к WebSocket."));
    }

    @Override
    protected void handleTextMessage (WebSocketSession session, TextMessage message) throws Exception {
        String chatUuid = (String) session.getAttributes().get("chatUuid");
        Principal principal = session.getPrincipal();
        String username = principal.getName();

        String payload = message.getPayload();
        String formattedMessage = username + ": " + payload;
        sendToText(chatUuid, formattedMessage);
    }

    @Override
    public void afterConnectionClosed (WebSocketSession session, CloseStatus status) throws Exception {
        String uuid = (String) session.getAttributes().get("chatUuid");

        Set<WebSocketSession> sessions = chatSessions.get(uuid);

        if (sessions == null) {
            sessions.remove(session);

            if (sessions.isEmpty()) {
                chatSessions.remove(uuid);
            }
        }

        System.out.println("Пользователь отключился от чата с UUID: " + uuid);
    }

    public void sendToText (String chatUuid, String message) {
        Set<WebSocketSession> sessions = chatSessions.get(chatUuid);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}