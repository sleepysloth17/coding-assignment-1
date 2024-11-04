package com.example.account.service;

import com.example.account.model.Account;
import com.example.account.model.Transaction;
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

  private final TransactionProxyService transactionProxyService;

  private final CustomerExistsValidator customerExistsValidator;

  private final LongValueValidator initialValueValidator;

  public AccountService(
      AccountRepository accountRepository,
      TransactionProxyService transactionProxyService,
      CustomerExistsValidator customerExistsValidator) {
    this.accountRepository = accountRepository;
    this.transactionProxyService = transactionProxyService;
    this.customerExistsValidator = customerExistsValidator;
    this.initialValueValidator = new LongValueValidator(0L, null);
  }

  public Account createAccount(UUID customerId, Long initialValue) {
    return ValidationRunner.from(customerExistsValidator, customerId)
        .and(initialValueValidator, initialValue)
        .ifValidOrThrow(() -> handleAccountCreation(customerId, initialValue));
  }

  private Account handleAccountCreation(UUID customerId, long initialValue) {
    final Account newAccount = accountRepository.save(new Account(null, customerId, initialValue));

    if (initialValue > 0) {
      try {
        final Transaction transaction =
            transactionProxyService.createTransactionForAccount(newAccount.getId(), initialValue);

        // throw to rollback account creation
        if (transaction == null) {
          throw new IllegalStateException(
              "Failed to create account: failed to create initial transaction");
        }
      } catch (Exception e) {
        // rollback account creation then rethrow the error
        deleteAccount(newAccount.getId());
        throw e;
      }
    }

    return newAccount;
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
