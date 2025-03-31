package com.jagt.hexagonal.bankapp.account.infrastructure.output.persistence.repository;

import com.jagt.hexagonal.bankapp.account.infrastructure.output.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
    List<AccountEntity> findByClient_Id(Long clientId);
}
