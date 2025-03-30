package com.jagt.hexagonal.bankapp.client.application.ports.input;

import com.jagt.hexagonal.bankapp.client.domain.model.Client;

import java.util.List;

public interface ClientServicePort {
    Client findClientById(Long id);
    List<Client> findAll();
    Client saveClient(Client client);
    Client updateClient(Long id, Client client);
    void deleteClientById(Long id);
}
