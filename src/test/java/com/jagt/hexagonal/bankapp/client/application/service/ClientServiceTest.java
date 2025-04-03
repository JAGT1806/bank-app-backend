package com.jagt.hexagonal.bankapp.client.application.service;

import com.jagt.hexagonal.bankapp.account.application.ports.output.AccountPersistencePort;
import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.client.application.ports.output.ClientPersistencePort;
import com.jagt.hexagonal.bankapp.client.domain.exception.AgeRestrictionException;
import com.jagt.hexagonal.bankapp.client.domain.exception.ClientHasAccountsException;
import com.jagt.hexagonal.bankapp.client.domain.exception.ClientNotFoundException;
import com.jagt.hexagonal.bankapp.client.domain.model.Client;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientPersistencePort clientPersistencePort;

    @Mock
    private AccountPersistencePort accountPersistencePort;

    @Mock
    private Validator validator;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("John");
        client.setLastname("Doe");
        client.setEmail("john.doe@example.com");
        client.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void findClientById_ShouldReturnClient_WhenClientExists() {
        when(clientPersistencePort.findById(1L)).thenReturn(Optional.of(client));

        Client found = clientService.findClientById(1L);

        assertNotNull(found);
        assertEquals(client.getId(), found.getId());
        verify(clientPersistencePort).findById(1L);
    }

    @Test
    void findClientById_ShouldThrowException_WhenClientNotFound() {
        when(clientPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.findClientById(1L));
    }

    @Test
    void saveClient_ShouldSave_WhenClientIsValid(){
        when(clientPersistencePort.save(any(Client.class))).thenReturn(client);

        Client saved = clientService.saveClient(client);

        assertNotNull(saved);
        verify(clientPersistencePort).save(client);
    }

    @Test
    void saveClient_ShouldThrowException_WhenUnderage() {
        client.setBirthday(LocalDate.now().minusYears(17));
        assertThrows(AgeRestrictionException.class, () -> clientService.saveClient(client));
    }


    @Test
    void deleteClientById_ShouldDelete_WhenClientHasNoAccounts() {
        when(accountPersistencePort.findByClient_Id(1L)).thenReturn(List.of());

        clientService.deleteClientById(1L);

        verify(clientPersistencePort).deleteById(1L);
    }

    @Test
    void deleteClientById_ShouldThrowException_WhenClientHasAccounts() {
        when(accountPersistencePort.findByClient_Id(1L)).thenReturn(List.of(new Account()));
        assertThrows(ClientHasAccountsException.class, () -> clientService.deleteClientById(1L));
    }
}