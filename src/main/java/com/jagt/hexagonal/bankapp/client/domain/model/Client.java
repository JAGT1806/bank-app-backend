package com.jagt.hexagonal.bankapp.client.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private LocalDateTime birthday;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
