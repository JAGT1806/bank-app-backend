package com.jagt.hexagonal.bankapp.client.infrastructure.output.persistence.mapper;

import com.jagt.hexagonal.bankapp.client.domain.model.Client;
import com.jagt.hexagonal.bankapp.client.infrastructure.output.persistence.entity.ClientEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientPersistenceMapper {
    ClientEntity toClientEntity(Client client);
    Client toClient(ClientEntity clientEntity);
    List<Client> toClientList(List<ClientEntity> clients);
}
