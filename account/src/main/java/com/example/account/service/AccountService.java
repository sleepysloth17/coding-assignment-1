package com.example.account.service;

import com.example.account.model.Account;
import com.example.account.repository.AccountRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account createAccount(UUID customerId, Long initialValue) {
    // TODO validate account creation and handle initial value stuff
    return accountRepository.save(null);
  }

  public Optional<Account> deleteAccount(UUID accountId) {
    final Optional<Account> account = getAccount(accountId);
    account.ifPresent(accountRepository::delete);
    return account;
  }

  public Optional<Account> getAccount(UUID accountId) {
    return accountRepository.findById(accountId);
  }

  public List<Account> getCustomerAccounts(UUID customerId) {
    return accountRepository.findByCustomerId(customerId);
  }
}
