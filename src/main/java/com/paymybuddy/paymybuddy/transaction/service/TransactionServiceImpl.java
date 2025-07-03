package com.paymybuddy.paymybuddy.transaction.service;

import com.paymybuddy.paymybuddy.connection.repository.UserConnectionRepository;
import com.paymybuddy.paymybuddy.transaction.model.*;
import com.paymybuddy.paymybuddy.transaction.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepo;
    private final TransactionRepository txRepo;
    private final UserConnectionRepository connRepo;

    public TransactionServiceImpl(UserRepository userRepo,
            TransactionRepository txRepo,
            UserConnectionRepository connRepo) {
        this.userRepo = userRepo;
        this.txRepo = txRepo;
        this.connRepo = connRepo;
    }

    @Transactional
    @Override
    public TransactionDTO create(CreateTransactionRequest req) {
        var sender = userRepo.findById(req.senderId()).orElseThrow();
        var receiver = userRepo.findByEmail(req.receiverEmail())
                .orElseThrow(() -> new IllegalArgumentException("Receiver email unknown"));

        if (sender.equals(receiver))
            throw new IllegalArgumentException("Cannot send money to yourself");

        if (!connRepo.existsByUserAndConnection(sender, receiver))
            throw new IllegalStateException("Receiver is not a buddy");

        var tx = new Transaction(null, sender, receiver,
                req.description(),
                req.amount(),
                Instant.now());

        var saved = txRepo.save(tx);
        return TransactionDTO.from(saved);
    }

    @Override
    public List<TransactionDTO> listForUser(Integer userId) {
        var user = userRepo.findById(userId).orElseThrow();
        return txRepo.findBySenderOrReceiverOrderByCreatedAtDesc(user, user)
                .stream().map(TransactionDTO::from).toList();
    }
}
