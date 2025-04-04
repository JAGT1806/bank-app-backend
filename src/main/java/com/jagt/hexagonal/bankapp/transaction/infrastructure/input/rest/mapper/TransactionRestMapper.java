package com.jagt.hexagonal.bankapp.transaction.infrastructure.input.rest.mapper;

import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;
import com.jagt.hexagonal.bankapp.transaction.infrastructure.input.rest.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionRestMapper {
    @Mapping(target = "sourceAccount", source = "sourceAccount.id")
    @Mapping(target = "destinationAccount", source = "destinationAccount.id")
    TransactionResponse toTransactionResponse(Transaction transaction);
    List<TransactionResponse> toTransactionResponseList(List<Transaction> transactions);
}
