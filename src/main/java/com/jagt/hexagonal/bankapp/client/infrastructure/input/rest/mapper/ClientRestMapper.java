package com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.mapper;

import com.jagt.hexagonal.bankapp.client.domain.model.Client;
import com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.request.ClientCreateRequest;
import com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.request.ClientUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientRestMapper {
    Client toClient(ClientCreateRequest request);
    Client toClient(ClientUpdateRequest request);
}
