package com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.controller;

import com.jagt.hexagonal.bankapp.client.application.ports.input.ClientServicePort;
import com.jagt.hexagonal.bankapp.client.domain.model.Client;
import com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.mapper.ClientRestMapper;
import com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.request.ClientCreateRequest;
import com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.request.ClientUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ClientController {
    private final ClientServicePort servicePort;
    private final ClientRestMapper mapper;

    @GetMapping
    public List<Client> listClients() {
        return servicePort.findAll();
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable long id) {
        return servicePort.findClientById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client createClient(@Valid @RequestBody ClientCreateRequest request) {
        return servicePort.saveClient(mapper.toClient(request));
    }

    @PutMapping("/{id}")
    public Client updateClient(@PathVariable long id, @Valid @RequestBody ClientUpdateRequest request) {
        return servicePort.updateClient(id, mapper.toClient(request));
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable long id) {
        servicePort.deleteClientById(id);
    }
}
