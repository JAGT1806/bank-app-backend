package com.jagt.hexagonal.bankapp.transaction.infrastructure.output.persistence.repository;

import com.jagt.hexagonal.bankapp.transaction.infrastructure.output.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
