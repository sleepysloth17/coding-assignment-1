package com.example.transaction.repository;

import com.example.transaction.model.Transaction;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InMemoryTransactionRepository
    extends CrudRepository<Transaction, UUID>, TransactionRepository {}
