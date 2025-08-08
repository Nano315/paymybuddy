package com.paymybuddy.paymybuddy.transaction.model;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionDTO(
        Integer id,
        Integer senderId,
        Integer receiverId,
        String senderUsername,
        String receiverUsername,
        BigDecimal amount,
        BigDecimal commission,
        String description,
        Instant createdAt) {
    public static TransactionDTO from(Transaction t) {
        BigDecimal commission = t.getAmount().multiply(new BigDecimal("0.005")); // 0,5 %
        return new TransactionDTO(
                t.getId(),
                t.getSender().getId(),
                t.getReceiver().getId(),
                t.getSender().getUsername(),
                t.getReceiver().getUsername(),
                t.getAmount(),
                commission,
                t.getDescription(),
                t.getCreatedAt());
    }
}
