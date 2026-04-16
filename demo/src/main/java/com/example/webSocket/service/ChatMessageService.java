package com.example.webSocket.service;

import com.example.webSocket.model.entity.ChatMessage;
import com.example.webSocket.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService (ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage (String chatUuid, String senderUsername, String content) {
        ChatMessage message = new ChatMessage(
                chatUuid,
                senderUsername,
                content,
                LocalDateTime.now()
        );

        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatHistory (String chatUuid) {
        return chatMessageRepository.findByChatUuidOrderByCreatedAtAsc(chatUuid);
    }
}