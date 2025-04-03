package com.jagt.hexagonal.bankapp.transaction.application.service;

import com.jagt.hexagonal.bankapp.account.application.ports.input.AccountServicePort;
import com.jagt.hexagonal.bankapp.transaction.application.ports.input.TransactionServicePort;
import com.jagt.hexagonal.bankapp.transaction.application.ports.output.TransactionPersistencePort;
import com.jagt.hexagonal.bankapp.transaction.domain.exception.TransactionNotFoundException;
import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;
import com.jagt.hexagonal.bankapp.transaction.domain.model.utils.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionServicePort {
    private final TransactionPersistencePort persistencePort;
    private final AccountServicePort accountService;

    @Override
    public List<Transaction> getTransactions() {
        return persistencePort.findAll();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return persistencePort.findById(id).orElseThrow(() -> new TransactionNotFoundException(null));
    }

    @Override
    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount) {
        accountService.deposit(accountId, amount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setSourceAccount(accountService.findAccountById(accountId));

        return persistencePort.save(transaction);
    }

    @Override
    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount) {
        accountService.withdraw(accountId, amount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setSourceAccount(accountService.findAccountById(accountId));

        return persistencePort.save(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {
        accountService.transfer(sourceAccountId, destinationAccountId, amount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setSourceAccount(accountService.findAccountById(sourceAccountId));
        transaction.setDestinationAccount(accountService.findAccountById(destinationAccountId));

        return persistencePort.save(transaction);
    }
}
