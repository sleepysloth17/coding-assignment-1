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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ValidatedAccountService implements IAccountService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ValidatedAccountService.class);

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
          LOGGER.error("Failed to create account: failed to create transaction");
          rollbackAccountCreation(createdAccount);
          return null;
        }

        LOGGER.info(
            "Created account with id {} and transaction with id {}",
            createdAccount.getId(),
            transaction.id());
        // Both account and transaction creation succeeded, so return the assembled dto
        return AccountDto.fromAccount(
            createdAccount, Collections.singletonList(TransactionDto.fromTransaction(transaction)));
      } catch (Exception e) {
        // rollback account creation then rethrow the error for end use consumption
        rollbackAccountCreation(createdAccount);
        throw e;
      }
    } else {
      // No transactions created, so don't need to include them on the dto
      LOGGER.info("Created account with id {} and no initial balance", createdAccount.getId());
      return AccountDto.fromAccount(createdAccount, Collections.emptyList());
    }
  }

  private void rollbackAccountCreation(Account account) {
    accountRepository.findById(account.getId()).ifPresent(accountRepository::delete);
    LOGGER.info("Creation of account {} failed, account creation rolled back", account.getId());
  }

  @Override
  public List<Account> getCustomerAccounts(UUID customerId) {
    return accountRepository.findByCustomerId(customerId);
  }
}
