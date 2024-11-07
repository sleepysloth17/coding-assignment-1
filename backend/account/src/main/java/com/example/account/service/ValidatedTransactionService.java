package com.example.account.service;

import com.example.account.dto.TransactionDto;
import com.example.account.model.Transaction;
import com.example.account.repository.AccountRepository;
import com.example.account.validation.ValidationRunner;
import com.example.account.validation.validator.AccountExistsValidator;
import com.example.account.validation.validator.TransactionTotalValidator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ValidatedTransactionService implements ITransactionService {

  private final TransactionProxyService transactionProxyService;

  private final AccountRepository accountRepository;

  private final AccountExistsValidator accountExistsValidator;

  private final TransactionTotalValidator transactionTotalValidator;

  public ValidatedTransactionService(
      TransactionProxyService transactionProxyService,
      AccountRepository accountRepository,
      AccountExistsValidator accountExistsValidator,
      TransactionTotalValidator transactionTotalValidator) {
    this.transactionProxyService = transactionProxyService;
    this.accountRepository = accountRepository;
    this.accountExistsValidator = accountExistsValidator;
    this.transactionTotalValidator = transactionTotalValidator;
  }

  @Override
  public TransactionDto createTransaction(UUID accountId, long amount) {
    return ValidationRunner.from(accountExistsValidator, accountId)
        .and(transactionTotalValidator, amount, accountId)
        .ifValidOrThrow(() -> this.handleTransactionCreation(accountId, amount));
  }

  private TransactionDto handleTransactionCreation(UUID accountId, long amount) {
    final Transaction transaction =
        transactionProxyService.createTransactionForAccount(accountId, amount);

    // If we failed to create the transaction, return null
    if (transaction == null) {
      return null;
    }

    accountRepository
        .findById(accountId)
        .ifPresent(
            account -> {
              account.setBalance(account.getBalance() + amount);
              accountRepository.save(account);
            });

    return TransactionDto.fromTransaction(transaction);
  }

  @Override
  public List<Transaction> getTransactions(UUID accountId) {
    return transactionProxyService.getAccountTransactions(accountId);
  }
}
