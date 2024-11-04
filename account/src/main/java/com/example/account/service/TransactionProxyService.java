package com.example.account.service;

import com.example.account.model.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TransactionProxyService {

  public Transaction createTransactionForAccount(UUID accountId, long amount) {
    return null; // TODO
  }

  public Optional<Transaction> deleteTransaction(UUID transactionId) {
    return Optional.empty(); // TODO
  }

  public Optional<Transaction> getTransaction(UUID transactionId) {
    return Optional.empty(); // TODO
  }

  public List<Transaction> getAccountTransactions(UUID accountId) {
    return List.of(); // TODO
  }
}
