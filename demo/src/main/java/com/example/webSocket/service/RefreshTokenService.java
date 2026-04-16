package com.example.webSocket.service;

import com.example.webSocket.config.jwt.JwtUtil;
import com.example.webSocket.model.entity.RefreshToken;
import com.example.webSocket.model.entity.User;
import com.example.webSocket.repository.RefreshTokenRepository;
import com.example.webSocket.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public RefreshTokenService (RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public RefreshToken create (String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));

        String token = jwtUtil.generateRefreshToken(username);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiredAt(LocalDateTime.now().plusDays(30));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validate (String token) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
                throw new RuntimeException("Token is expired!");
            }

            RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid refresh token!"));

            if (refreshToken.isRevoked()) {
                throw new RuntimeException("Refresh token is revoked!");
            }

            if (refreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Refresh token is expired!");
            }

            return refreshToken;
        }

        catch (Exception e) {
            throw new RuntimeException("Token validation failed: " + e.getMessage());
        }
    }

    @Transactional
    public void revoke (RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeAll (User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }
}