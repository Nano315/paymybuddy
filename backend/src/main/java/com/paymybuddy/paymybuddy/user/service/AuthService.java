package com.paymybuddy.paymybuddy.user.service;

import com.paymybuddy.paymybuddy.config.security.JwtService;
import com.paymybuddy.paymybuddy.user.model.*;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UserRepository repo,
            BCryptPasswordEncoder encoder,
            AuthenticationManager authManager,
            JwtService jwtService) {
        this.repo = repo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    /* ---------- SIGNUP ---------- */
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

    /* ---------- LOGIN ---------- */
    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));

        var user = repo.findByEmail(req.email()).orElseThrow();
        String token = jwtService.generate(user.getEmail(), user.getId());
        return new AuthResponse(token);
    }
}
