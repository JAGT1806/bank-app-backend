package com.jagt.hexagonal.bankapp.account.application.ports.input;

import com.jagt.hexagonal.bankapp.account.domain.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountServicePort {
    Account findAccountById(Long id);
    List<Account> findAllAccounts();
    List<Account> findAccountsByClient_Id(Long clientId);
    Account createAccount(Account account);
    void deposit(Long id, BigDecimal amount);
    void withdraw(Long id, BigDecimal amount);
    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
    void activateAccount(Long id);
    void deactivateAccount(Long id);
    void cancelAccount(Long id);
}
