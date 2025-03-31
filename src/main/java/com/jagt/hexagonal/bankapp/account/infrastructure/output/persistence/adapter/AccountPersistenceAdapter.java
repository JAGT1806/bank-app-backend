package com.jagt.hexagonal.bankapp.account.infrastructure.output.persistence.adapter;

import com.jagt.hexagonal.bankapp.account.application.ports.output.AccountPersistencePort;
import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.account.infrastructure.output.persistence.mapper.AccountPersistenceMapper;
import com.jagt.hexagonal.bankapp.account.infrastructure.output.persistence.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountPersistencePort {
    private final AccountRepository repository;
    private final AccountPersistenceMapper mapper;

    @Override
    public Optional<Account> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toAccount);
    }

    @Override
    public List<Account> findAll() {
        return mapper.toAccountList(repository.findAll());
    }

    @Override
    public Account save(Account account) {
        return mapper.toAccount(repository.save(mapper.toAccountEntity(account)));
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .map(mapper::toAccount);
    }

    @Override
    public List<Account> findByClient_Id(Long clientId) {
        return mapper.toAccountList(repository.findByClient_Id(clientId));
    }
}
