package com.example.webSocket.controller.auth;

import com.example.webSocket.config.jwt.JwtUtil;
import com.example.webSocket.model.dto.auth.request.LoginDto;
import com.example.webSocket.model.entity.RefreshToken;
import com.example.webSocket.service.RefreshTokenService;
import com.example.webSocket.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtutil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController (JwtUtil jwtutil, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtutil = jwtutil;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginDto loginDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword())
            );
        } catch (BadCredentialsException exception) {
            log.warn("Ошибка входа: неверный логин или пароль для пользователя: {}", loginDto.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль!");
        }

        String accessToken = jwtutil.generateAccessToken(loginDto.getUsername());
        RefreshToken refreshToken = refreshTokenService.create(loginDto.getUsername());

        ResponseCookie accessCookie = ResponseCookie
                .from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/website-websocket")
                .maxAge(15 * 60)
                .sameSite("Lax")
                .build();

        ResponseCookie responseCookie = ResponseCookie
                .from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/website-websocket")
                .maxAge(30L * 24 * 60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh (HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            RefreshToken refreshTokenEntity = refreshTokenService.validate(refreshToken);

            if (refreshTokenEntity == null) {
                return ResponseEntity.badRequest().build();
            }

            String username = jwtutil.extractUsername(refreshToken);

            String newAccessToken = jwtutil.generateAccessToken(username);

            ResponseCookie accessCookie = ResponseCookie
                    .from("accessToken", newAccessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/website-websocket")
                    .maxAge(15 * 60)
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            return ResponseEntity.ok().build();
        }

        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenValue = extractRefreshTokenFromCookie(request);

        if (refreshTokenValue != null) {
            try {
                RefreshToken refreshTokenEntity = refreshTokenService.validate(refreshTokenValue);
                refreshTokenService.revoke(refreshTokenEntity);
            } catch (Exception e) {
                System.out.println("Ошибка при выходе из системы: " + e.getMessage());
            }
        }

        ResponseCookie accessCookie = ResponseCookie
                .from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/website-websocket")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/website-websocket")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().build();
    }

    private String extractRefreshTokenFromCookie (HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}