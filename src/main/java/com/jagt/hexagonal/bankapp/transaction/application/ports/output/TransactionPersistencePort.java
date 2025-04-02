package com.jagt.hexagonal.bankapp.transaction.application.ports.output;

import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionPersistencePort {
    List<Transaction> findAll();
    Optional<Transaction> findById(Long id);
    Transaction save(Transaction transaction);
}
