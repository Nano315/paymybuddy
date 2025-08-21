package com.paymybuddy.paymybuddy.user.service;

import com.paymybuddy.paymybuddy.user.model.UpdateProfileRequest;
import com.paymybuddy.paymybuddy.user.model.UserDTO;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public List<UserDTO> listAll() {
        return repo.findAll().stream().map(UserDTO::from).toList();
    }

    @Override
    public UserDTO getByEmail(String email) {
        var user = repo.findByEmail(email).orElseThrow();
        return UserDTO.from(user);
    }

    @Override
    public UserDTO updateProfile(String currentEmail, UpdateProfileRequest req) {
        var user = repo.findByEmail(currentEmail).orElseThrow();

        // Unicité email / username si modifiés
        if (!user.getEmail().equals(req.email()) && repo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }
        if (!user.getUsername().equals(req.username()) && repo.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username déjà utilisé");
        }

        user.setUsername(req.username());
        user.setEmail(req.email());

        if (req.password() != null && !req.password().isBlank()) {
            user.setPasswordHash(encoder.encode(req.password()));
        }

        var saved = repo.save(user);
        return UserDTO.from(saved);
    }
}
