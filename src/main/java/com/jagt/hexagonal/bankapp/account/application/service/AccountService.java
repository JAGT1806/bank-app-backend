package com.jagt.hexagonal.bankapp.account.application.service;

import com.jagt.hexagonal.bankapp.account.application.ports.input.AccountServicePort;
import com.jagt.hexagonal.bankapp.account.application.ports.output.AccountPersistencePort;
import com.jagt.hexagonal.bankapp.account.domain.exception.AccountNotActivatedException;
import com.jagt.hexagonal.bankapp.account.domain.exception.AccountNotFoundException;
import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.account.domain.model.utils.AccountStatus;
import com.jagt.hexagonal.bankapp.account.domain.model.utils.AccountType;
import com.jagt.hexagonal.bankapp.client.application.ports.input.ClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountServicePort {
    private final AccountPersistencePort accountPersistencePort;
    private final ClientServicePort clientServicePort;

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
        clientServicePort.findClientById(account.getClient().getId());

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
        validateActiveAccount(account);
        account.setBalance(account.getBalance().add(amount));
        account.setUpdatedAt(LocalDateTime.now());

        accountPersistencePort.save(account);
    }

    @Transactional
    @Override
    public void withdraw(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor a 0.");
        }

        Account account = findAccountById(id);
        validateActiveAccount(account);
        BigDecimal amountToWithdraw = amount;

        if(!account.isGmfExempt()) {
            BigDecimal gmf = amount.multiply(new BigDecimal("0.004")); // 4 x 1000
            amountToWithdraw = amount.add(gmf);
        }

        if (account.getBalance().compareTo(amountToWithdraw) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el retiro.");
        }

        account.setBalance(account.getBalance().subtract(amountToWithdraw));
        account.setUpdatedAt(LocalDateTime.now());

        accountPersistencePort.save(account);
    }

    @Transactional
    @Override
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a transferir debe ser mayor a 0.");
        }

        Account fromAccount = findAccountById(fromAccountId);
        validateActiveAccount(fromAccount);
        Account toAccount = findAccountById(toAccountId);
        validateActiveAccount(toAccount);
        BigDecimal amountToTransfer = amount;

        if(!fromAccount.isGmfExempt()) {
            BigDecimal gmf = amount.multiply(new BigDecimal("0.004")); // 4x1000
            amountToTransfer = amount.add(gmf);
        }

        if (fromAccount.getBalance().compareTo(amountToTransfer) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar la transferencia.");
        }


        fromAccount.setBalance(fromAccount.getBalance().subtract(amountToTransfer));
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

    private void validateActiveAccount(Account account) {
        if(account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActivatedException(null);
        }
    }

}
