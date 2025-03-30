package com.jagt.hexagonal.bankapp.transaction.domain.model;

import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.transaction.domain.model.utils.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private Account accountOrigin;
    private Account accountDestination;
}
