package com.jagt.hexagonal.bankapp.client.infrastructure.output.persistence.repository;

import com.jagt.hexagonal.bankapp.client.infrastructure.output.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
}
