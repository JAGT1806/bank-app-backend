package com.jagt.hexagonal.bankapp.account.application.ports.output;

import com.jagt.hexagonal.bankapp.account.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountPersistencePort {
    Optional<Account> findById(Long id);
    List<Account> findAll();
    Account save(Account account);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByClient_Id(Long clientId);
}
