package com.jagt.hexagonal.bankapp.transaction.infrastructure.output.persistence.mapper;

import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;
import com.jagt.hexagonal.bankapp.transaction.infrastructure.output.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionPersistenceMapper {
    @Mapping(target = "sourceAccount", ignore = true)
    @Mapping(target = "destinationAccount", ignore = true)
    TransactionEntity toEntity(Transaction transaction);
    Transaction toDomain(TransactionEntity entity);
    List<Transaction> toDomain(List<TransactionEntity> entities);
}
