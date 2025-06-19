package com.paymybuddy.paymybuddy.user.repository;

import com.paymybuddy.paymybuddy.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> { }
