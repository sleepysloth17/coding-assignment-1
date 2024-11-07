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
public class TransactionService {

  private final TransactionProxyService transactionProxyService;

  private final AccountRepository accountRepository;

  private final AccountExistsValidator accountExistsValidator;

  private final TransactionTotalValidator transactionTotalValidator;

  public TransactionService(
      TransactionProxyService transactionProxyService,
      AccountRepository accountRepository,
      AccountExistsValidator accountExistsValidator,
      TransactionTotalValidator transactionTotalValidator) {
    this.transactionProxyService = transactionProxyService;
    this.accountRepository = accountRepository;
    this.accountExistsValidator = accountExistsValidator;
    this.transactionTotalValidator = transactionTotalValidator;
  }

  // TODO
  public TransactionDto createTransactionForAccount(UUID accountId, long amount) {
    return ValidationRunner.from(accountExistsValidator, accountId)
        .and(transactionTotalValidator, amount, accountId)
        .ifValidOrThrow(
            () -> {
              accountRepository
                  .findById(accountId)
                  .ifPresent(
                      account -> {
                        account.setBalance(account.getBalance() + amount);
                        accountRepository.save(account);
                      });

              return TransactionDto.fromTransaction(
                  transactionProxyService.createTransactionForAccount(accountId, amount));
            });
  }

  public List<Transaction> getAccountTransactions(UUID accountId) {
    return transactionProxyService.getAccountTransactions(accountId);
  }
}
