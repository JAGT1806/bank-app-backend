package com.jagt.hexagonal.bankapp.client.application.service;

import com.jagt.hexagonal.bankapp.client.application.ports.input.ClientServicePort;
import com.jagt.hexagonal.bankapp.client.application.ports.output.ClientPersistencePort;
import com.jagt.hexagonal.bankapp.client.domain.exception.AgeRestrictionException;
import com.jagt.hexagonal.bankapp.client.domain.exception.ClientNotFoundException;
import com.jagt.hexagonal.bankapp.client.domain.exception.InvalidBirthdayException;
import com.jagt.hexagonal.bankapp.client.domain.model.Client;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientServicePort {
    private final ClientPersistencePort clientPersistencePort;

    @Override
    public Client findClientById(Long id) {
        return clientPersistencePort.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(null));
    }

    @Override
    public List<Client> findAll() {
        return clientPersistencePort.findAll();
    }

    @Override
    public Client saveClient(Client client) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(client.getBirthday(), today);

        if(client.getBirthday().isBefore(today))
            throw new InvalidBirthdayException(null);


        if(period.getYears() < 18)
            throw new AgeRestrictionException(null);


        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        return clientPersistencePort.save(client);
    }

    @Override
    public Client updateClient(Long id, Client client) {
        return clientPersistencePort.findById(id)
                .map(savedClient -> {
                    if(StringUtils.isNotBlank(client.getName()))
                        savedClient.setName(client.getName());

                    if(StringUtils.isNotBlank(client.getLastname()))
                        savedClient.setLastname(client.getLastname());

                    if(StringUtils.isNotBlank(client.getEmail()))
                        savedClient.setEmail(client.getEmail());


                    savedClient.setUpdatedAt(LocalDateTime.now());
                    return clientPersistencePort.save(savedClient);
                })
                .orElseThrow();
    }

    @Override
    public void deleteClientById(Long id) {
        clientPersistencePort.deleteById(id);
    }
}
