package com.paymybuddy.paymybuddy.connection.repository;

import com.paymybuddy.paymybuddy.connection.model.*;
import com.paymybuddy.paymybuddy.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserConnectionRepository
        extends JpaRepository<UserConnection, UserConnectionKey> {

    List<UserConnection> findByUser(User user);

    boolean existsByUserAndConnection(User user, User connection);
}