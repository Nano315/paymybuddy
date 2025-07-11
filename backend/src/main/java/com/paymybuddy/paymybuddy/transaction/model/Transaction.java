package com.paymybuddy.paymybuddy.transaction.model;

import com.paymybuddy.paymybuddy.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@org.hibernate.annotations.Check(constraints = "amount > 0 AND sender_id <> receiver_id")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Transaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;          // montant brut transféré

    @Column(name = "created_at", nullable = false,
             updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt = Instant.now();
}
