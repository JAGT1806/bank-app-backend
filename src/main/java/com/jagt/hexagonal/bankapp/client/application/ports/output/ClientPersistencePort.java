package com.jagt.hexagonal.bankapp.client.application.ports.output;

import com.jagt.hexagonal.bankapp.client.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientPersistencePort {
    Optional<Client> findById(Long id);
    List<Client> findAll();
    Client save(Client client);
    void deleteById(Long id);
}
