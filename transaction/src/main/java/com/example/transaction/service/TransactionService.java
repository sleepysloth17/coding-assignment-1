package com.example.transaction.service;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

  public Optional<Transaction> getTransaction(UUID transactionId) {
    return transactionRepository.findById(transactionId);
  }
}
