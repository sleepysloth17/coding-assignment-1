package com.example.account.dto;

import com.example.account.model.Transaction;
import java.time.Instant;
import java.util.UUID;

public record TransactionDto(UUID id, Instant createdAt, UUID accountId, long amount) {
  public static TransactionDto fromTransaction(Transaction transaction) {
    return new TransactionDto(
        transaction.id(), transaction.createdAt(), transaction.accountId(), transaction.amount());
  }
}
