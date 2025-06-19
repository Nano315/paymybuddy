package com.paymybuddy.paymybuddy.user.service;

import com.paymybuddy.paymybuddy.user.model.UserDTO;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) { this.repo = repo; }

    @Override
    public List<UserDTO> listAll() {
        return repo.findAll().stream().map(UserDTO::from).toList();
    }
}
