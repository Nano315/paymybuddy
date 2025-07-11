package com.paymybuddy.paymybuddy.user.controller;

import com.paymybuddy.paymybuddy.user.model.UserDTO;
import com.paymybuddy.paymybuddy.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @GetMapping
    public List<UserDTO> getAll() {
        return service.listAll();
    }
}

