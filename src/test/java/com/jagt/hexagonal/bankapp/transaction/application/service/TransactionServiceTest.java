package com.jagt.hexagonal.bankapp.transaction.application.service;

import com.jagt.hexagonal.bankapp.account.application.ports.input.AccountServicePort;
import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.transaction.application.ports.output.TransactionPersistencePort;
import com.jagt.hexagonal.bankapp.transaction.domain.exception.TransactionNotFoundException;
import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;
import com.jagt.hexagonal.bankapp.transaction.domain.model.utils.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionPersistencePort persistencePort;

    @Mock
    private AccountServicePort accountService;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(1000));

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(BigDecimal.valueOf(500));
    }

    @Test
    void getTransactions_ShouldReturnListOfTransactions() {
        when(persistencePort.findAll()).thenReturn(List.of(transaction));
        List<Transaction> transactions = transactionService.getTransactions();
        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
    }

    @Test
    void getTransactionById_ShouldReturnTransaction_WhenTransactionExists() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(transaction));
        Transaction found = transactionService.getTransactionById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void getTransactionById_ShouldThrowException_WhenTransactionDoesNotExist() {
        when(persistencePort.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(1L));
    }

    @Test
    void deposit_ShouldCreateTransactionAndIncreaseBalance() {
        when(accountService.findAccountById(1L)).thenReturn(account);
        transaction.setType(TransactionType.DEPOSIT);
        when(persistencePort.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.deposit(1L, BigDecimal.valueOf(500));

        assertNotNull(result);
        assertEquals(TransactionType.DEPOSIT, result.getType());
        verify(accountService).deposit(1L, BigDecimal.valueOf(500));
        verify(persistencePort).save(any(Transaction.class));
    }

    @Test
    void withdraw_ShouldCreateTransactionAndDecreaseBalance() {
        when(accountService.findAccountById(1L)).thenReturn(account);
        transaction.setType(TransactionType.WITHDRAWAL);
        when(persistencePort.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.withdraw(1L, BigDecimal.valueOf(500));

        assertNotNull(result);
        assertEquals(TransactionType.WITHDRAWAL, result.getType());
        verify(accountService).withdraw(1L, BigDecimal.valueOf(500));
        verify(persistencePort).save(any(Transaction.class));
    }

    @Test
    void transfer_ShouldCreateTransactionAndMoveFunds() {
        Account destination = new Account();
        destination.setId(2L);
        destination.setBalance(BigDecimal.valueOf(500));

        when(accountService.findAccountById(1L)).thenReturn(account);
        when(accountService.findAccountById(2L)).thenReturn(destination);
        transaction.setType(TransactionType.TRANSFER);
        when(persistencePort.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.transfer(1L, 2L, BigDecimal.valueOf(500));

        assertNotNull(result);
        assertEquals(TransactionType.TRANSFER, result.getType());
        verify(accountService).transfer(1L, 2L, BigDecimal.valueOf(500));
        verify(persistencePort).save(any(Transaction.class));
    }

}