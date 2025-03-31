package com.jagt.hexagonal.bankapp.account.infrastructure.input.rest.controller;

import com.jagt.hexagonal.bankapp.account.application.ports.input.AccountServicePort;
import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.account.infrastructure.input.rest.mapper.AccountRestMapper;
import com.jagt.hexagonal.bankapp.account.infrastructure.input.rest.request.AccountCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountServicePort accountServicePort;
    private final AccountRestMapper accountRestMapper;

    @GetMapping
    public List<Account> findAll() {
        return accountServicePort.findAllAccounts();
    }

    @GetMapping("/{id}")
    public Account findById(@PathVariable Long id) {
        return accountServicePort.findAccountById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<Account> findByClientId(@PathVariable Long clientId) {
        return accountServicePort.findAccountsByClient_Id(clientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@Valid @RequestBody AccountCreateRequest request) {
        return accountServicePort.createAccount(accountRestMapper.toAccount(request));
    }

    @PutMapping("/{id}/activate")
    public void activate(@PathVariable Long id) {
        accountServicePort.activateAccount(id);
    }

    @PutMapping("/{id}/deactive")
    public void deactivate(@PathVariable Long id) {
        accountServicePort.deactivateAccount(id);
    }

    @PutMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        accountServicePort.cancelAccount(id);
    }

}
