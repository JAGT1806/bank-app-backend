package com.jagt.hexagonal.bankapp.client.domain.model;

import com.jagt.hexagonal.bankapp.client.domain.model.utils.IdentificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    private Long id;
    private IdentificationType identificationType;
    private String identificationNumber;
    private String name;
    private String lastname;
    private String email;
    private LocalDate birthday;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
