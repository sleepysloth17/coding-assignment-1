package com.example.transaction.repository;

import com.example.transaction.model.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

  Optional<Transaction> findById(UUID id);

  void delete(Transaction transaction);

  Transaction save(Transaction transaction);

  List<Transaction> findByAccountId(UUID accountId);
}
