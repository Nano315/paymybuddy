package com.paymybuddy.paymybuddy.user.controller;

import com.paymybuddy.paymybuddy.user.model.UpdateProfileRequest;
import com.paymybuddy.paymybuddy.user.model.UserDTO;
import com.paymybuddy.paymybuddy.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return service.listAll();
    }

    // Récupérer son profil courant
    @GetMapping("/me")
    public UserDTO me(Authentication auth) {
        String email = auth.getName(); // principal = email
        return service.getByEmail(email);
    }

    // Mettre à jour son profil courant
    @PutMapping("/me")
    public UserDTO updateMe(@Valid @RequestBody UpdateProfileRequest req, Authentication auth) {
        String email = auth.getName();
        return service.updateProfile(email, req);
    }
}
