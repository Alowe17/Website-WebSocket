package com.example.webSocket.controller.api;

import com.example.webSocket.model.dto.auth.request.RegisterDto;
import com.example.webSocket.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class RegisterApiController {
    private final RegisterService registerService;

    public RegisterApiController(RegisterService registerService) {
        this.registerService = registerService;
    }
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto register) {
        registerService.create(register);
        return ResponseEntity.ok().build();
    }
}