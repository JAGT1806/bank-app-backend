package com.jagt.hexagonal.bankapp.account.infrastructure.input.rest.mapper;

import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.account.infrastructure.input.rest.request.AccountCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountRestMapper {
    @Mapping(target = "client.id", source = "clientId")
    Account toAccount(AccountCreateRequest request);
}
