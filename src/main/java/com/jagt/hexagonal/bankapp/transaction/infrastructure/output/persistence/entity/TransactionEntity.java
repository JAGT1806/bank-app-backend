package com.jagt.hexagonal.bankapp.transaction.infrastructure.output.persistence.entity;

import com.jagt.hexagonal.bankapp.account.infrastructure.output.persistence.entity.AccountEntity;
import com.jagt.hexagonal.bankapp.transaction.domain.model.utils.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    @Column(nullable = false)
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private AccountEntity sourceAccount;
    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private AccountEntity destinationAccount;
    @Column(nullable = false)
    private LocalDateTime transactionDate;
}
