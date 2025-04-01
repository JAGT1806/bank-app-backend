package com.jagt.hexagonal.bankapp.account.application.service;

import com.jagt.hexagonal.bankapp.account.application.ports.input.AccountServicePort;
import com.jagt.hexagonal.bankapp.account.application.ports.output.AccountPersistencePort;
import com.jagt.hexagonal.bankapp.account.domain.exception.AccountNotFoundException;
import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.account.domain.model.utils.AccountStatus;
import com.jagt.hexagonal.bankapp.account.domain.model.utils.AccountType;
import com.jagt.hexagonal.bankapp.client.application.ports.output.ClientPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountServicePort {
    private final AccountPersistencePort accountPersistencePort;
    private final ClientPersistencePort clientPersistencePort;

    @Override
    public Account findAccountById(Long id) {
        return accountPersistencePort.findById(id).orElseThrow(() -> new AccountNotFoundException(null));
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountPersistencePort.findAll();
    }

    @Override
    public List<Account> findAccountsByClient_Id(Long clientId) {
        return accountPersistencePort.findByClient_Id(clientId);
    }

    @Override
    public Account createAccount(Account account) {
        clientPersistencePort.findById(account.getClient().getId());

        account.setAccountNumber(generateAccountNumber(account.getAccountType()));

        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        if(account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }

        if(account.getAccountType() == AccountType.SAVING) {
            account.setStatus(AccountStatus.ACTIVE);
        } else {
            account.setStatus(AccountStatus.INACTIVE);
        }

        validateAccount(account);

        return accountPersistencePort.save(account);
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor a 0.");
        }

        Account account = findAccountById(id);
        account.setBalance(account.getBalance().add(amount));
        account.setUpdatedAt(LocalDateTime.now());

        accountPersistencePort.save(account);
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor a 0.");
        }

        Account account = findAccountById(id);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el retiro.");
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedAt(LocalDateTime.now());

        accountPersistencePort.save(account);
    }

    @Override
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a transferir debe ser mayor a 0.");
        }

        Account fromAccount = findAccountById(fromAccountId);
        Account toAccount = findAccountById(toAccountId);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar la transferencia.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        fromAccount.setUpdatedAt(LocalDateTime.now());
        toAccount.setUpdatedAt(LocalDateTime.now());

        accountPersistencePort.save(fromAccount);
        accountPersistencePort.save(toAccount);
    }


    @Override
    public void activateAccount(Long id) {
        Account account = findAccountById(id);
        account.setStatus(AccountStatus.ACTIVE);
        account.setUpdatedAt(LocalDateTime.now());
        accountPersistencePort.save(account);
    }

    @Override
    public void deactivateAccount(Long id) {
        Account account = findAccountById(id);
        account.setStatus(AccountStatus.INACTIVE);
        account.setUpdatedAt(LocalDateTime.now());
        accountPersistencePort.save(account);
    }

    @Override
    public void cancelAccount(Long id) {
        Account account = findAccountById(id);

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Solo se puede cancelar una cuenta con saldo $0.");
        }

        account.setStatus(AccountStatus.CANCELED);
        account.setUpdatedAt(LocalDateTime.now());
        accountPersistencePort.save(account);
    }

    private String generateAccountNumber(AccountType type) {
        Random random = new Random();
        int num = 10000000 + random.nextInt(90000000); // Número aleatorio de 8 dígitos
        return (type == AccountType.SAVING ? "53" : "33") + num;
    }

    private void validateAccount(Account account) {
        if (account.getBalance() == null) {
            throw new IllegalArgumentException("El saldo de la cuenta no puede ser nulo.");
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo de la cuenta no puede ser negativo.");
        }

        if (account.getAccountType() == AccountType.SAVING && account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo de una cuenta de ahorros no puede ser menor a $0.");
        }

        if (account.getAccountNumber() == null || !account.getAccountNumber().matches("^(53|33)\\d{8}$")) {
            throw new IllegalArgumentException("El número de cuenta debe tener 10 dígitos y comenzar con '53' (ahorro) o '33' (corriente).");
        }
    }

}
