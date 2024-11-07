package com.example.account.service;

import com.example.account.dto.AccountDto;
import com.example.account.dto.TransactionDto;
import com.example.account.model.Account;
import com.example.account.model.Transaction;
import com.example.account.repository.AccountRepository;
import com.example.account.validation.ValidationRunner;
import com.example.account.validation.validator.CustomerExistsValidator;
import com.example.account.validation.validator.LongValueValidator;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ValidatedAccountService implements IAccountService {

  private final AccountRepository accountRepository;

  private final TransactionProxyService transactionProxyService;

  private final CustomerExistsValidator customerExistsValidator;

  private final LongValueValidator initialValueValidator;

  public ValidatedAccountService(
      AccountRepository accountRepository,
      TransactionProxyService transactionProxyService,
      CustomerExistsValidator customerExistsValidator) {
    this.accountRepository = accountRepository;
    this.transactionProxyService = transactionProxyService;
    this.customerExistsValidator = customerExistsValidator;
    this.initialValueValidator = new LongValueValidator(0L, null);
  }

  @Override
  public AccountDto createAccount(UUID customerId, Long initialValue) {
    return ValidationRunner.from(customerExistsValidator, customerId)
        .and(initialValueValidator, initialValue)
        .ifValidOrThrow(() -> handleAccountCreation(customerId, initialValue));
  }

  private AccountDto handleAccountCreation(UUID customerId, long initialValue) {
    final Account newAccount = new Account();
    newAccount.setCustomerId(customerId);
    newAccount.setBalance(initialValue);

    final Account createdAccount = accountRepository.save(newAccount);

    if (initialValue > 0) {
      try {
        final Transaction transaction =
            transactionProxyService.createTransactionForAccount(
                createdAccount.getId(), initialValue);

        // throw to rollback account creation
        if (transaction == null) {
          throw new IllegalStateException(
              "Failed to create account: failed to create initial transaction");
        }

        // Both account and transaction creation succeeded, so return the assembled dto
        return AccountDto.fromAccount(
            createdAccount, Collections.singletonList(TransactionDto.fromTransaction(transaction)));
      } catch (Exception e) {
        // rollback account creation then rethrow the error for end use consumption
        accountRepository.findById(createdAccount.getId()).ifPresent(accountRepository::delete);
        throw e;
      }
    } else {
      // No transactions created, so don't need to include them ont he dto
      return AccountDto.fromAccount(createdAccount, Collections.emptyList());
    }
  }

  @Override
  public List<Account> getCustomerAccounts(UUID customerId) {
    return accountRepository.findByCustomerId(customerId);
  }
}
