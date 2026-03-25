package com.example.webSocket.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatUuid;
    private String senderUsername;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();

    public  ChatMessage() {}
    public ChatMessage(String chatUuid, String senderUsername, String content, LocalDateTime createdAt) {
        this.chatUuid = chatUuid;
        this.senderUsername = senderUsername;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getChatUuid() {
        return chatUuid;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatUuid(String chatUuid) {
        this.chatUuid = chatUuid;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}