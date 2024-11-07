package com.example.account.service;

import com.example.account.dto.TransactionDto;
import com.example.account.model.Transaction;
import java.util.List;
import java.util.UUID;

public interface ITransactionService {
  TransactionDto createTransaction(UUID accountId, long amount);

  List<Transaction> getTransactions(UUID accountId);
}
