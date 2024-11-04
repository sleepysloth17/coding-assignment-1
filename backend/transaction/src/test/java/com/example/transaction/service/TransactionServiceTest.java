package com.example.transaction.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @InjectMocks private TransactionService transactionService;

  @Mock private TransactionRepository transactionRepository;

  private UUID accountId;

  @BeforeEach
  void setUp() {
    accountId = UUID.randomUUID();
  }

  @Test
  void shouldReturnCreatedTransaction() {
    when(transactionRepository.save(any())).then(returnsFirstArg());

    final Transaction result = transactionService.createTransactionForAccount(accountId, 10L);

    assertNotNull(result);
    assertThat(result.getAccountId(), is(accountId));
    assertThat(result.getAmount(), is(10L));
  }

  @Test
  void shouldReturnAccountTransactions() {
    final List<Transaction> transactionList =
        List.of(
            new Transaction(UUID.randomUUID(), accountId, 10L),
            new Transaction(UUID.randomUUID(), accountId, 11L),
            new Transaction(UUID.randomUUID(), accountId, 12L));

    when(transactionRepository.findByAccountId(accountId)).thenReturn(transactionList);

    final List<Transaction> result = transactionService.getAccountTransactions(accountId);

    assertNotNull(result);
    assertThat(result, is(transactionList));
  }
}
