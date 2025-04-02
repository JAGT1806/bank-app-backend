package com.jagt.hexagonal.bankapp.transaction.infrastructure.input.rest.controller;

import com.jagt.hexagonal.bankapp.transaction.application.ports.input.TransactionServicePort;
import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionServicePort servicePort;

    @GetMapping
    public List<Transaction> getTransactions() {

    }

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable Long id) {

    }

    @PostMapping("/deposit/{accountId}")
    public Transaction deposit(@PathVariable Long accountId, @RequestBody BigDecimal amount) {
        return servicePort.deposit(accountId, amount);
    }

    @PostMapping("/withdraw/{accountId}")
    public Transaction withdraw(@PathVariable Long accountId, @RequestBody BigDecimal amount) {
        return servicePort.withdraw(accountId, amount);
    }

    @PostMapping("/transfer/{sourceAccountId}/{destinationAccountId}")
    public Transaction transfer(@PathVariable Long sourceAccountId, @PathVariable Long destinationAccountId, @RequestBody BigDecimal amount) {
        return servicePort.transfer(sourceAccountId, destinationAccountId, amount);
    }
}
