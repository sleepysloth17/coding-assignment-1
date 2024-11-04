package com.example.account.service;

import com.example.account.model.Account;
import com.example.account.model.Transaction;
import com.example.account.repository.AccountRepository;
import com.example.account.validation.ValidationException;
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

  private final TransactionService transactionService;

  private final CustomerExistsValidator customerExistsValidator;

  private final LongValueValidator initialValueValidator;

  public AccountService(
      AccountRepository accountRepository,
      TransactionService transactionService,
      CustomerExistsValidator customerExistsValidator) {
    this.accountRepository = accountRepository;
    this.transactionService = transactionService;
    this.customerExistsValidator = customerExistsValidator;
    this.initialValueValidator = new LongValueValidator(0L, null);
  }

  public Account createAccount(UUID customerId, Long initialValue) {
    return ValidationRunner.from(customerExistsValidator, customerId)
        .and(initialValueValidator, initialValue)
        .ifValidOrThrow(() -> handleAccountCreation(customerId, initialValue));
  }

  private Account handleAccountCreation(UUID customerId, long initialValue) {
    final Account newAccount = accountRepository.save(new Account(null, customerId));

    if (initialValue > 0) {
      try {
        // handle if this errors please
        final Transaction transaction =
            transactionService.createTransactionForAccount(newAccount.getId(), initialValue);

        // failed to create a transaction for some reason, rollback account creation then throw
        if (transaction == null) {
          deleteAccount(newAccount.getId());
          throw new IllegalStateException("Failed to create account: error during initial transaction creation");
        }
      } catch (ValidationException validationException) {
        // if we have a validation error, we want to delete the account and then rethrow the
        // validation error to return
        // to the user.
        deleteAccount(newAccount.getId());
        throw validationException;
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
