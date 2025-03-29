package com.jagt.hexagonal.bankapp.transaction.domain.model;

import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.transaction.domain.model.utils.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private Account accountOrigin;
    private Account accountDestination;
}
