package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.account.model.Account;
import com.example.account.model.Transaction;
import com.example.account.service.AccountService;
import com.example.account.service.TransactionService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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

  @Mock private AccountService accountService;

  private UUID accountId;

  @BeforeEach
  void setUp() {
    accountId = UUID.randomUUID();
  }

  @Test
  void shouldReturnCreatedTransaction() {
    final Transaction transaction = getTransaction(accountId, 10L);

    when(transactionService.createTransactionForAccount(accountId, 10L)).thenReturn(transaction);

    final ResponseEntity<Transaction> response =
        transactionController.createTransactionForAccount(accountId, 10L);

    assertNotNull(response);
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(transaction));
  }

  @Test
  void shouldReturnAccountTransactionsIfAccountExists() {
    final List<Transaction> transactionList =
        List.of(
            getTransaction(accountId, 10L),
            getTransaction(accountId, 11L),
            getTransaction(accountId, 12L));

    when(accountService.getAccount(accountId)).thenReturn(Optional.of(getAccount()));
    when(transactionService.getAccountTransactions(accountId)).thenReturn(transactionList);

    final ResponseEntity<List<Transaction>> response =
        transactionController.getAllTransactionsForAccount(accountId);

    assertNotNull(response);
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(transactionList));
  }

  @Test
  void shouldReturn404IfAccountDoesNotExist() {
    when(accountService.getAccount(accountId)).thenReturn(Optional.empty());

    final ResponseEntity<List<Transaction>> response =
        transactionController.getAllTransactionsForAccount(accountId);

    assertNotNull(response);
    assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  private Account getAccount() {
    final Account account = new Account();
    account.setId(accountId);
    account.setCustomerId(UUID.randomUUID());
    account.setBalance(0L);
    return account;
  }

  private Transaction getTransaction(UUID accountId, long amount) {
    return new Transaction(UUID.randomUUID(), Instant.now(), accountId, amount);
  }
}
