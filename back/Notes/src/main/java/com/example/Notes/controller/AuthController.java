package com.example.Notes.controller;

import com.example.Notes.dto.TokenResponseDto;
import com.example.Notes.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    private final AuthService authService;

    @Value("${controller.endpoints.login}")
    private String loginEndpoint;

    @Value("${controller.endpoints.register}")
    private String registerEndpoint;

    @Value("${controller.endpoints.refresh}")
    private String refreshEndpoint;

    @Value("${controller.endpoints.logout}")
    private String logoutEndpoint;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("${controller.endpoints.login}")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            TokenResponseDto tokens = authService.login(username, password);
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException e) {
            if ("Invalid credentials".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверные учетные данные");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка авторизации");
        }
    }

    @PostMapping("${controller.endpoints.register}")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        try {
            String username = registerRequest.get("username");
            String password = registerRequest.get("password");
            TokenResponseDto tokens = authService.register(username, password);
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException e) {
            if ("User already exists".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь уже существует");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка регистрации");
        }
    }

    @PostMapping("${controller.endpoints.refresh}")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");
            TokenResponseDto tokens = authService.refresh(refreshToken);
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный refresh токен");
        }
    }

    @PostMapping("${controller.endpoints.logout}")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}