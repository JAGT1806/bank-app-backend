package com.jagt.hexagonal.bankapp.transaction.application.ports.input;

import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionServicePort {
    List<Transaction> getTransactions();
    Transaction getTransactionById(Long id);
    Transaction deposit(Long accountId, BigDecimal amount);
    Transaction withdraw(Long accountId, BigDecimal amount);
    Transaction transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount);
}
