package com.paymybuddy.paymybuddy.connection.service;

import com.paymybuddy.paymybuddy.connection.model.*;
import com.paymybuddy.paymybuddy.connection.repository.UserConnectionRepository;
import com.paymybuddy.paymybuddy.user.model.*;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserConnectionServiceImpl implements UserConnectionService {

    private final UserRepository userRepo;
    private final UserConnectionRepository connRepo;

    public UserConnectionServiceImpl(UserRepository userRepo,
            UserConnectionRepository connRepo) {
        this.userRepo = userRepo;
        this.connRepo = connRepo;
    }

    @Override
    public List<UserDTO> listFriends(Integer userId) {
        var user = userRepo.findById(userId).orElseThrow();
        return connRepo.findByUser(user).stream()
                .map(c -> UserDTO.from(c.getConnection()))
                .toList();
    }

    @Transactional
    @Override
    public void addFriend(Integer userId, String friendEmail) {
        var user = userRepo.findById(userId).orElseThrow();
        var friend = userRepo.findByEmail(friendEmail)
                .orElseThrow(() -> new IllegalArgumentException("Friend email unknown"));

        if (user.equals(friend))
            throw new IllegalArgumentException("Cannot add yourself");

        if (connRepo.existsByUserAndConnection(user, friend))
            throw new IllegalStateException("Already friends");

        // lien direct + lien miroir (pour requêtes symétriques)
        var c1 = new UserConnection(user, friend);
        var c2 = new UserConnection(user, friend);
        connRepo.save(c1);
        connRepo.save(c2);
    }
}
