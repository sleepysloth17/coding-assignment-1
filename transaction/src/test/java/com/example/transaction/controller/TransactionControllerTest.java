package com.example.transaction.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.transaction.model.Transaction;
import com.example.transaction.service.TransactionService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

  @InjectMocks private TransactionController transactionController;

  @Mock private TransactionService transactionService;

  private UUID accountId;

  @BeforeEach
  void setUp() {
    accountId = UUID.randomUUID();
  }

  @Test
  void shouldReturnCreatedTransaction() {
    final Transaction transaction = new Transaction(UUID.randomUUID(), accountId, 10L);

    when(transactionService.createTransactionForAccount(accountId, 10L)).thenReturn(transaction);

    final ResponseEntity<Transaction> response =
            transactionController.createTransactionForAccount(accountId, 10L);

    assertNotNull(response);
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(transaction));
  }

  @Test
  void shouldReturnAccountTransactions() {
    final List<Transaction> transactionList =
            List.of(
                    new Transaction(UUID.randomUUID(), accountId, 10L),
                    new Transaction(UUID.randomUUID(), accountId, 11L),
                    new Transaction(UUID.randomUUID(), accountId, 12L));

    when(transactionService.getAccountTransactions(accountId)).thenReturn(transactionList);

    final ResponseEntity<List<Transaction>> response =
            transactionController.getAllTransactionsForAccount(accountId);

    assertNotNull(response);
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(transactionList));
  }
}
