package com.jagt.hexagonal.bankapp.account.application.service;

import com.jagt.hexagonal.bankapp.account.application.ports.output.AccountPersistencePort;
import com.jagt.hexagonal.bankapp.account.domain.exception.AccountNotFoundException;
import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.account.domain.model.utils.AccountStatus;
import com.jagt.hexagonal.bankapp.account.domain.model.utils.AccountType;
import com.jagt.hexagonal.bankapp.client.application.ports.output.ClientPersistencePort;
import com.jagt.hexagonal.bankapp.client.domain.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountPersistencePort accountPersistencePort;
    @Mock
    private ClientPersistencePort clientPersistencePort;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountType(AccountType.SAVING);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setStatus(AccountStatus.ACTIVE);
        account.setGmfExempt(true);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void finAccountById_ShouldReturnAccount_WhenAccountExists() {
        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(account));
        Account found = accountService.findAccountById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void findAccountById_ShouldThrowException_WhenAccountDoesNotExist() {
        when(accountPersistencePort.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.findAccountById(1L));
    }

    @Test
    void createAccount_ShouldGenerateValidAccountNumber() {
        Account account = new Account();
        account.setAccountType(AccountType.SAVING);
        Client client = new Client();
        client.setId(1L);
        account.setClient(client);

        when(clientPersistencePort.findById(1L)).thenReturn(Optional.of(client));
        when(accountPersistencePort.save(any(Account.class))).thenReturn(account);

        Account created = accountService.createAccount(account);

        assertTrue(created.getAccountNumber().startsWith("53"));
        assertEquals(10, created.getAccountNumber().length());
    }

    @Test
    void createAccount_ShouldGenerateValidCheckingAccountNumber() {
        Account account = new Account();
        account.setAccountType(AccountType.CHECKING);
        Client client = new Client();
        client.setId(1L);
        account.setClient(client);

        when(clientPersistencePort.findById(1L)).thenReturn(Optional.of(client));
        when(accountPersistencePort.save(any(Account.class))).thenReturn(account);

        Account created = accountService.createAccount(account);

        assertTrue(created.getAccountNumber().startsWith("33"));
        assertEquals(10, created.getAccountNumber().length());
    }

    @Test
    void deposit_ShouldIncreaseBalance_WhenAmountIsValid() {
        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(account));
        accountService.deposit(1L, BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(1500), account.getBalance());
        verify(accountPersistencePort).save(account);
    }

    @Test
    void withdraw_ShouldDecreaseBalance_WhenAmountIsValid() {
        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(account));
        accountService.withdraw(1L, BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500), account.getBalance());
        verify(accountPersistencePort).save(account);
    }

    @Test
    void withdraw_ShouldThrowException_WhenAmountIsNotValid() {
        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(account));
        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(1L, BigDecimal.valueOf(2000)));
    }

    @Test
    void transfer_ShouldMoveFundsBetweenAccounts() {
        Account to = new Account();
        to.setId(2L);
        to.setBalance(BigDecimal.valueOf(500));

        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(account));
        when(accountPersistencePort.findById(2L)).thenReturn(Optional.of(to));

        accountService.transfer(1L, 2L, BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500), account.getBalance());
        assertEquals(BigDecimal.valueOf(1000), to.getBalance());
        verify(accountPersistencePort, times(2)).save(any(Account.class));
    }

    @Test
    void activateAccount_ShouldChangeStatusToActive() {
        account.setStatus(AccountStatus.INACTIVE);
        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(account));

        accountService.activateAccount(1L);
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        verify(accountPersistencePort).save(account);
    }

}