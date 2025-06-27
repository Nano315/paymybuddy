package com.paymybuddy.paymybuddy.user.service;

import com.paymybuddy.paymybuddy.user.model.*;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class AuthService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;

    @Value("${app.jwt.secret:my-super-secret-key}") // clé par défaut (dev)
    private String jwtSecret;

    public AuthService(UserRepository repo,
            BCryptPasswordEncoder encoder,
            AuthenticationManager authManager) {
        this.repo = repo;
        this.encoder = encoder;
        this.authManager = authManager;
    }

    public UserDTO register(RegisterRequest req) {
        if (repo.existsByEmail(req.email()))
            throw new IllegalArgumentException("Email déjà utilisé");
        if (repo.existsByUsername(req.username()))
            throw new IllegalArgumentException("Username déjà utilisé");

        var user = new User(null,
                req.username(),
                req.email(),
                encoder.encode(req.password()),
                Instant.now());
        var saved = repo.save(user);
        return UserDTO.from(saved);
    }

    public AuthResponse login(LoginRequest req) {
        // vérifie les identifiants via Spring Security
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));

        var user = repo.findByEmail(req.email()).orElseThrow();
        String token = Jwts.builder()
                .subject(user.getEmail())
                .claim("uid", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 h
                .compact();
        return new AuthResponse(token);
    }
}
