package com.example.account.repository;

import com.example.account.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    Optional<Account> findById(UUID id);

    void delete(Account account);

    Account save(Account account);

    List<Account> findByCustomerId(UUID customerId);
}
