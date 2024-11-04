package com.example.transaction.service;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;

  public TransactionService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public Transaction createTransactionForAccount(UUID accountId, long amount) {
    return transactionRepository.save(new Transaction(null, accountId, amount));
  }

  public List<Transaction> getAccountTransactions(UUID accountId) {
    return transactionRepository.findByAccountId(accountId);
  }
}
