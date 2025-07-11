package com.paymybuddy.paymybuddy.transaction.controller;

import com.paymybuddy.paymybuddy.transaction.model.*;
import com.paymybuddy.paymybuddy.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    /* ---------- POST envoyer de l’argent ---------- */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDTO create(@Valid @RequestBody CreateTransactionRequest req) {
        return service.create(req);
    }

    /* ---------- GET historique ---------- */
    @GetMapping("/{userId}")
    public List<TransactionDTO> list(@PathVariable Integer userId) {
        return service.listForUser(userId);
    }
}