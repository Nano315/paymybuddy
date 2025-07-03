package com.paymybuddy.paymybuddy.transaction.repository;

import com.paymybuddy.paymybuddy.transaction.model.Transaction;
import com.paymybuddy.paymybuddy.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySenderOrReceiverOrderByCreatedAtDesc(User sender, User receiver);
}