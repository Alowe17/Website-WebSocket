package com.example.webSocket.service;

import com.example.webSocket.model.Chat;
import com.example.webSocket.repository.ChatRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService (ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public boolean userHasAccess (String uuid, String username) {
        Chat chat = chatRepository.findByUuid(uuid).orElse(null);

        if (chat == null) {
            return false;
        }

        return chat.getOwnerUsername().equals(username);
    }
}