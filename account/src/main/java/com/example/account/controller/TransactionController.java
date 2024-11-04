package com.example.account.controller;

import com.example.account.model.Transaction;
import com.example.account.service.AccountService;
import com.example.account.service.TransactionService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

  private final TransactionService transactionService;

  private final AccountService accountService;

  public TransactionController(
      TransactionService transactionService, AccountService accountService) {
    this.transactionService = transactionService;
    this.accountService = accountService;
  }

  @PostMapping("/accounts/{accountId}/transactions")
  public ResponseEntity<Transaction> createTransactionForAccount(
      @PathVariable(value = "accountId") UUID accountId,
      @RequestParam(value = "amount", required = true) long amount) {

    return ResponseEntity.ok(transactionService.createTransactionForAccount(accountId, amount));
  }

  @GetMapping("/accounts/{accountId}/transactions")
  public ResponseEntity<List<Transaction>> getAllTransactionsForAccount(
      @PathVariable(value = "accountId") UUID accountId) {
    return ResponseEntity.of(
        accountService
            .getAccount(accountId)
            .map(account -> transactionService.getAccountTransactions(accountId)));
  }
}
