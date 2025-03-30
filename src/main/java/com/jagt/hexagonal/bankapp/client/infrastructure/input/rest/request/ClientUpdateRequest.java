package com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ClientUpdateRequest(
        @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
        String name,

        @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
        String lastname,

        @Email(message = "El correo electrónico no es válido")
        String email
) {
}
