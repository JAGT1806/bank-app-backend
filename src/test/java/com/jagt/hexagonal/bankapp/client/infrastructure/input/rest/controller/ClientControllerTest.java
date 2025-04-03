package com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jagt.hexagonal.bankapp.client.application.ports.input.ClientServicePort;
import com.jagt.hexagonal.bankapp.client.domain.model.Client;
import com.jagt.hexagonal.bankapp.client.domain.model.utils.IdentificationType;
import com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.mapper.ClientRestMapper;
import com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.request.ClientCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ClientController.class)
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientServicePort clientServicePort;

    @MockitoBean
    private ClientRestMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Client client;
    private ClientCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("John");
        client.setLastname("Doe");
        client.setEmail("john.doe@example.com");
        client.setBirthday(LocalDate.of(2000, 1, 1));

        createRequest = new ClientCreateRequest(
                IdentificationType.DNI,
                "123456789",
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDate.of(2000, 1, 1));
    }

    @Test
    void listClients_ShouldReturnClients() throws Exception {
        when(clientServicePort.findAll()).thenReturn(List.of(client));

        mockMvc.perform(get("/api/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(client.getName()));

        verify(clientServicePort).findAll();
    }

    @Test
    void createClient_ShouldReturnCreatedClient() throws Exception {
        when(mapper.toClient(any(ClientCreateRequest.class))).thenReturn(client);
        when(clientServicePort.saveClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(client.getName()));

        verify(clientServicePort).saveClient(any(Client.class));
    }

    @Test
    void deleteClient_ShouldReturnNoContent() throws Exception {
        doNothing().when(clientServicePort).deleteClientById(1L);

        mockMvc.perform(delete("/api/client/1"))
                .andExpect(status().isOk());

        verify(clientServicePort).deleteClientById(1L);
    }

}