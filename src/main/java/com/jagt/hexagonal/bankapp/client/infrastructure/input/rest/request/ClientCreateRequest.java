package com.jagt.hexagonal.bankapp.client.infrastructure.input.rest.request;

import com.jagt.hexagonal.bankapp.client.domain.model.utils.IdentificationType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClientCreateRequest(
        @NotNull(message = "El tipo de identificación es obligatorio")
        IdentificationType identificationType,

        @NotBlank(message = "El número de identificación es obligatorio")
        @Size(min = 5, max = 20, message = "El número de identificación debe tener entre 5 y 20 carácteres")
        String identificationNumber,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
        String lastname,

        @NotBlank(message = "El correo electrónico es obligatio")
        @Email(message = "El formato de correo electrónico no es válido")
        String email,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe estar en el pasado")
        LocalDate birthday
) {
}
