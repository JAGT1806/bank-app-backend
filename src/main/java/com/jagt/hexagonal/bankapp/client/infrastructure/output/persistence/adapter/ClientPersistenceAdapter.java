package com.jagt.hexagonal.bankapp.client.infrastructure.output.persistence.adapter;

import com.jagt.hexagonal.bankapp.client.application.ports.output.ClientPersistencePort;
import com.jagt.hexagonal.bankapp.client.domain.model.Client;
import com.jagt.hexagonal.bankapp.client.infrastructure.output.persistence.mapper.ClientPersistenceMapper;
import com.jagt.hexagonal.bankapp.client.infrastructure.output.persistence.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientPersistenceAdapter implements ClientPersistencePort {
    private final ClientRepository repository;
    private final ClientPersistenceMapper mapper;

    @Override
    public Optional<Client> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toClient);
    }

    @Override
    public List<Client> findAll() {
        return mapper.toClientList(repository.findAll());
    }

    @Override
    public Client save(Client client) {
        return mapper.toClient(repository.save(mapper.toClientEntity(client)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
