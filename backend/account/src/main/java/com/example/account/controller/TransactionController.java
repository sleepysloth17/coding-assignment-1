package com.example.account.controller;

import com.example.account.dto.TransactionDto;
import com.example.account.service.ITransactionService;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

  private final ITransactionService transactionService;

  public TransactionController(ITransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping("/accounts/{accountId}/transactions")
  public ResponseEntity<TransactionDto> createTransactionForAccount(
      @PathVariable(value = "accountId") UUID accountId,
      @RequestParam(value = "amount", required = true) long amount) {
    return ResponseEntity.ok(transactionService.createTransaction(accountId, amount));
  }
}
