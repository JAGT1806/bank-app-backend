package com.jagt.hexagonal.bankapp.transaction.infrastructure.input.rest.controller;

import com.jagt.hexagonal.bankapp.transaction.application.ports.input.TransactionServicePort;
import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;
import com.jagt.hexagonal.bankapp.transaction.infrastructure.input.rest.mapper.TransactionRestMapper;
import com.jagt.hexagonal.bankapp.transaction.infrastructure.input.rest.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionServicePort servicePort;
    private final TransactionRestMapper mapper;

    @GetMapping
    public List<TransactionResponse> getTransactions() {
        return mapper.toTransactionResponseList(servicePort.getTransactions());
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransaction(@PathVariable Long id) {
        return mapper.toTransactionResponse(servicePort.getTransactionById(id));
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
