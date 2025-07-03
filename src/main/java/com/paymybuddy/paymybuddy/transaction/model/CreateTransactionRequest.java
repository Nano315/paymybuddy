package com.paymybuddy.paymybuddy.transaction.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateTransactionRequest(
        Integer senderId,
        @NotBlank @Email String receiverEmail,
        @Positive BigDecimal amount,
        String description) {
}
