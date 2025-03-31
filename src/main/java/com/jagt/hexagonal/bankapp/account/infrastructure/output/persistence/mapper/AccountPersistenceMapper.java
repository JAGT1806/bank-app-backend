package com.jagt.hexagonal.bankapp.account.infrastructure.output.persistence.mapper;

import com.jagt.hexagonal.bankapp.account.domain.model.Account;
import com.jagt.hexagonal.bankapp.account.infrastructure.output.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountPersistenceMapper {
    AccountEntity toAccountEntity(Account account);
    Account toAccount(AccountEntity accountEntity);
    List<Account> toAccountList(List<AccountEntity> accountEntities);
}
