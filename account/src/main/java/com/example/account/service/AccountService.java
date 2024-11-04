package com.example.account.service;

import com.example.account.model.Account;
import com.example.account.repository.AccountRepository;
import com.example.account.validation.ValidationRunner;
import com.example.account.validation.validator.CustomerExistsValidator;
import com.example.account.validation.validator.LongValueValidator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;

  private final CustomerExistsValidator customerExistsValidator;

  private final LongValueValidator initialValueValidator;

  public AccountService(
      AccountRepository accountRepository, CustomerExistsValidator customerExistsValidator) {
    this.accountRepository = accountRepository;
    this.customerExistsValidator = customerExistsValidator;
    this.initialValueValidator = new LongValueValidator(0L, null);
  }

  public Account createAccount(UUID customerId, Long initialValue) {
    return ValidationRunner.from(customerExistsValidator, customerId)
        .and(initialValueValidator, initialValue)
        .ifValidOrThrow(() -> accountRepository.save(new Account(null, customerId)));
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
