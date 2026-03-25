package com.example.webSocket.repository;

import com.example.webSocket.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findById(Long id);
    List<ChatMessage> findByChatUuid(String chatUuid);
    List<ChatMessage> findBySenderUsername(String senderUsername);
    List<ChatMessage> findByChatUuidOrderByCreatedAtAsc(String chatUuid);
}