package com.example.webSocket.service;

import com.example.webSocket.model.dto.auth.request.RegisterDto;
import com.example.webSocket.model.entity.User;
import com.example.webSocket.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public RegisterService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public void create (RegisterDto register) {
        if (userRepository.findByUsername(register.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (register.getUsername().isBlank()) {
            throw new RuntimeException("Имя пользователя не может быть пустым");
        }

        if (register.getPassword().isBlank()) {
            throw new RuntimeException("Пароль не может быть пустым");
        }

        User user = new User();
        user.setUsername(register.getUsername());
        user.setPassword(encoder.encode(register.getPassword()));
        userRepository.save(user);
    }
}