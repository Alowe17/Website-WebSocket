package com.example.webSocket.repository;

import com.example.webSocket.model.entity.RefreshToken;
import com.example.webSocket.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteAllByUser(User user);
}