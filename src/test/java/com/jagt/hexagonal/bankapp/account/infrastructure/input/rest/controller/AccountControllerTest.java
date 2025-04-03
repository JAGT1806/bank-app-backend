package com.jagt.hexagonal.bankapp.account.infrastructure.input.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jagt.hexagonal.bankapp.account.application.ports.input.AccountServicePort;
import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.account.domain.model.utils.AccountStatus;
import com.jagt.hexagonal.bankapp.account.domain.model.utils.AccountType;
import com.jagt.hexagonal.bankapp.account.infrastructure.input.rest.mapper.AccountRestMapper;
import com.jagt.hexagonal.bankapp.account.infrastructure.input.rest.request.AccountCreateRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountServicePort accountServicePort;

    @MockitoBean
    private AccountRestMapper mapper;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountType(AccountType.SAVING);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setStatus(AccountStatus.ACTIVE);
    }

    @Test
    void findAll_ShouldReturnListOfAccounts() throws Exception {
        when(accountServicePort.findAllAccounts()).thenReturn(List.of(account));

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findById_ShouldReturnAccount_WhenExists() throws Exception {
        when(accountServicePort.findAccountById(1L)).thenReturn(account);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_ShouldReturnCreatedAccount() throws Exception {
        AccountCreateRequest request = new AccountCreateRequest(
                1L,
                AccountType.SAVING,
                BigDecimal.valueOf(500),
                true
        );

        when(mapper.toAccount(any(AccountCreateRequest.class))).thenReturn(account);
        when(accountServicePort.createAccount(any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void activate_ShouldReturnNoContent() throws Exception {
        doNothing().when(accountServicePort).activateAccount(1L);

        mockMvc.perform(put("/api/accounts/1/activate"))
                .andExpect(status().isOk());
    }

}