package com.jagt.hexagonal.bankapp.transaction.infrastructure.input.rest.controller;

import com.jagt.hexagonal.bankapp.transaction.application.ports.input.TransactionServicePort;
import com.jagt.hexagonal.bankapp.transaction.domain.model.Transaction;
import com.jagt.hexagonal.bankapp.transaction.domain.model.utils.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionServicePort transactionServicePort;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(BigDecimal.valueOf(500));
        transaction.setType(TransactionType.DEPOSIT);
    }

    @Test
    void getTransactions_ShouldReturnListOfTransactions() throws Exception {
        when(transactionServicePort.getTransactions()).thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("DEPOSIT"));
    }

    @Test
    void getTransaction_ShouldReturnTransaction() throws Exception {
        when(transactionServicePort.getTransactionById(1L)).thenReturn(transaction);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("DEPOSIT"));
    }

    @Test
    void deposit_ShouldReturnCreatedTransaction() throws Exception {
        when(transactionServicePort.deposit(1L, BigDecimal.valueOf(500))).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions/deposit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("DEPOSIT"));
    }

    @Test
    void deposit_ShouldReturnBadRequest_WhenNegativeAmount() throws Exception {
        when(transactionServicePort.deposit(1L, BigDecimal.valueOf(-100))).thenThrow(new IllegalArgumentException("Datos de la solicitud inválidos"));

        mockMvc.perform(post("/api/transactions/deposit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("-100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Datos de la solicitud inválidos"));
    }

    @Test
    void withdraw_ShouldReturnTransaction() throws Exception {
        when(transactionServicePort.withdraw(1L, BigDecimal.valueOf(500))).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("DEPOSIT"));
    }

    @Test
    void transfer_ShouldReturnTransaction() throws Exception {
        when(transactionServicePort.transfer(1L, 2L, BigDecimal.valueOf(500))).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions/transfer/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("DEPOSIT"));
    }
}