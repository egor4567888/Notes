package com.example.Notes.service;

import com.example.Notes.dto.TokenResponseDto;
import com.example.Notes.model.User;
import com.example.Notes.repository.UserRepository;
import com.example.Notes.security.TokenProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public TokenResponseDto login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
            User user = userOpt.get();
            String accessToken = tokenProvider.createAccessToken(user);
            String refreshToken = tokenProvider.createRefreshToken(user);
            return new TokenResponseDto(accessToken, refreshToken);
        }
        throw new RuntimeException("Invalid credentials");
    }

    public TokenResponseDto register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user);
        return new TokenResponseDto(accessToken, refreshToken);
    }

    public TokenResponseDto refresh(String refreshToken) {
        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromToken(refreshToken);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String newAccessToken = tokenProvider.createAccessToken(user);
            String newRefreshToken = tokenProvider.createRefreshToken(user);
            return new TokenResponseDto(newAccessToken, newRefreshToken);
        }
        throw new RuntimeException("Invalid refresh token");
    }
}