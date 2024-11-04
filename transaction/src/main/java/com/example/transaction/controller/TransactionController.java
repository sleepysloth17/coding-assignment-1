package com.example.transaction.controller;

import com.example.transaction.model.Transaction;
import com.example.transaction.service.TransactionService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping("/accounts/{accountId}/transactions")
  public ResponseEntity<Transaction> createTransactionForAccount(
      @PathVariable(value = "accountId") UUID accountId, @RequestBody Long amount) {
    return ResponseEntity.ok(transactionService.createTransactionForAccount(accountId, amount));
  }

  @GetMapping("/accounts/{accountId}/transactions")
  public ResponseEntity<List<Transaction>> getAllTransactionsForAccount(
      @PathVariable(value = "accountId") UUID accountId) {
    return ResponseEntity.ok(transactionService.getAccountTransactions(accountId));
  }

  @GetMapping("/transactions/{transactionId}")
  public ResponseEntity<Transaction> getTransaction(
      @PathVariable(value = "transactionId") UUID transactionId) {
    return ResponseEntity.of(transactionService.getTransaction(transactionId));
  }
}
