package com.paymybuddy.paymybuddy.user.controller;

import com.paymybuddy.paymybuddy.user.model.*;
import com.paymybuddy.paymybuddy.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    /* ---------- SIGNUP ---------- */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO register(@Valid @RequestBody RegisterRequest req) {
        return service.register(req);
    }

    /* ---------- LOGIN ---------- */
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return service.login(req);
    }
}
