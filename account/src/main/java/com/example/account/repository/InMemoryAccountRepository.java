package com.example.account.repository;

import com.example.account.model.Account;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InMemoryAccountRepository
    extends CrudRepository<Account, UUID>, AccountRepository {}
