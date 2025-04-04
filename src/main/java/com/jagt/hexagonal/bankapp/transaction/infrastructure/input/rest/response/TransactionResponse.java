package com.jagt.hexagonal.bankapp.transaction.infrastructure.input.rest.response;

import com.jagt.hexagonal.bankapp.transaction.domain.model.utils.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        TransactionType type,
        BigDecimal amount,
        Long sourceAccount,
        Long destinationAccount,
        LocalDateTime transactionDate
) {
}
