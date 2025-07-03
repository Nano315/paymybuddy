package com.paymybuddy.paymybuddy.transaction.service;

import com.paymybuddy.paymybuddy.transaction.model.*;
import java.util.List;

public interface TransactionService {
    TransactionDTO create(CreateTransactionRequest req);

    List<TransactionDTO> listForUser(Integer userId);
}
