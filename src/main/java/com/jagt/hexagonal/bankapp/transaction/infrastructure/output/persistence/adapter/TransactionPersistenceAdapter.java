package com.jagt.hexagonal.bankapp.transaction.infrastructure.output.persistence.adapter;

import com.jagt.hexagonal.bankapp.transaction.application.ports.output.TransactionPersistencePort;
import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;
import com.jagt.hexagonal.bankapp.transaction.infrastructure.output.persistence.mapper.TransactionPersistenceMapper;
import com.jagt.hexagonal.bankapp.transaction.infrastructure.output.persistence.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionPersistencePort {
    private final TransactionRepository transactionRepository;
    private final TransactionPersistenceMapper mapper;

    @Override
    public List<Transaction> findAll() {
        return mapper.toDomain(transactionRepository.findAll());
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return mapper.toDomain(transactionRepository.save(mapper.toEntity(transaction)));
    }
}
