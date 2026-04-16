package com.example.webSocket.service;

import com.example.webSocket.model.entity.User;
import com.example.webSocket.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void updateUserPassword (String username, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User getUserUsername (String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));
    }
}