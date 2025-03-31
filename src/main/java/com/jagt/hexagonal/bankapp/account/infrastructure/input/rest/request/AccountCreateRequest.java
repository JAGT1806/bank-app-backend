package com.jagt.hexagonal.bankapp.account.infrastructure.input.rest.request;

import com.jagt.hexagonal.bankapp.account.domain.model.utils.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountCreateRequest(
        @NotNull(message = "El ID del cliente es oligatorio")
        Long clientId,

        @NotNull(message = "El tipo de cuenta es oligatorio")
        AccountType accountType,

        @DecimalMin(value = "0.0", message = "El saldo no puede ser menor a 0")
        BigDecimal balance,

        @NotNull(message = "Debe especificar si la cuenta es exenta de GMF")
        boolean gmfExempt
) {
}
