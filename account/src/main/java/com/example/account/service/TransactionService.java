package com.example.account.service;

import com.example.account.model.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  private final TransactionProxyService transactionProxyService;

  public TransactionService(TransactionProxyService transactionProxyService) {
    this.transactionProxyService = transactionProxyService;
  }

  public Transaction createTransactionForAccount(UUID accountId, long amount) {
    // TODO - if the amount is less than 0, we want to validate that we have enough in the account
    return transactionProxyService.createTransactionForAccount(accountId, amount);
  }

  public List<Transaction> getAccountTransactions(UUID accountId) {
    return transactionProxyService.getAccountTransactions(accountId);
  }
}
