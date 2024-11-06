package com.example.account.dto;

import com.example.account.model.Account;
import java.util.List;
import java.util.UUID;

public record AccountDto(
    UUID id, UUID customerId, long balance, List<TransactionDto> transactions) {

  public static AccountDto fromAccount(Account account, List<TransactionDto> transactions) {
    return new AccountDto(
        account.getId(), account.getCustomerId(), account.getBalance(), transactions);
  }
}
