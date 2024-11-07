package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.account.dto.TransactionDto;
import com.example.account.service.ITransactionService;
import java.time.Instant;
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

  @Mock private ITransactionService transactionService;

  private UUID accountId;

  @BeforeEach
  void setUp() {
    accountId = UUID.randomUUID();
  }

  @Test
  void shouldReturnCreatedTransaction() {
    final TransactionDto transaction =
        new TransactionDto(UUID.randomUUID(), Instant.now(), UUID.randomUUID(), 0L);

    when(transactionService.createTransaction(accountId, 10L)).thenReturn(transaction);

    final ResponseEntity<TransactionDto> response =
        transactionController.createTransactionForAccount(accountId, 10L);

    assertNotNull(response);
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(transaction));
  }
}
