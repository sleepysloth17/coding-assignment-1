package com.example.account.service;

import com.example.account.model.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    public Account createAccount(UUID customerId) {
        return null; // TODO
    }

    public Optional<Account> deleteAccount(UUID accountId) {
        return Optional.empty(); // TODO
    }

    public Optional<Account> getAccount(UUID accountId) {
        return Optional.empty(); // TODO
    }

    public List<Account> getCustomerAccounts(UUID customerId) {
        return List.of(); // TODO
    }
}
