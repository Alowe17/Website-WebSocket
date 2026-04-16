package com.example.webSocket.repository;

import com.example.webSocket.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,String> {
    Optional<Chat> findByUuid (String uuid);
}