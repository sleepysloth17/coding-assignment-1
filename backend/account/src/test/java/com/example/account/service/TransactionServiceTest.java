package com.example.account.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.account.model.Account;
import com.example.account.model.Transaction;
import com.example.account.repository.AccountRepository;
import com.example.account.validation.ValidationException;
import com.example.account.validation.ValidationResponse;
import com.example.account.validation.validator.AccountExistsValidator;
import com.example.account.validation.validator.TransactionTotalValidator;
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

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @InjectMocks private TransactionService transactionService;

  @Mock private TransactionProxyService transactionProxyService;

  @Mock private AccountRepository accountRepository;

  @Mock private AccountExistsValidator accountExistsValidator;

  @Mock private TransactionTotalValidator transactionTotalValidator;

  private UUID accountId;

  @BeforeEach
  void setUp() {
    accountId = UUID.randomUUID();
  }

  @Test
  void createTransactionShouldThrowIfAccountDoesNotExist() {
    when(accountExistsValidator.validate(accountId)).thenReturn(new ValidationResponse(false, ""));
    when(transactionTotalValidator.validate(10L, accountId))
        .thenReturn(new ValidationResponse(true, ""));

    assertThrows(
        ValidationException.class,
        () -> transactionService.createTransactionForAccount(accountId, 10L));

    verify(transactionProxyService, never()).createTransactionForAccount(any(), anyLong());
  }

  @Test
  void createTransactionShouldThrowIfTotalIsInvalid() {
    when(accountExistsValidator.validate(accountId)).thenReturn(new ValidationResponse(true, ""));
    when(transactionTotalValidator.validate(10L, accountId))
        .thenReturn(new ValidationResponse(false, ""));

    assertThrows(
        ValidationException.class,
        () -> transactionService.createTransactionForAccount(accountId, 10L));

    verify(transactionProxyService, never()).createTransactionForAccount(any(), anyLong());
  }

  @Test
  void createTransactionShouldUpdateAccountAndCreateTransactionIfIsValidRequest() {
    final Transaction transaction = getTransaction();
    final Account account = new Account();
    account.setId(accountId);

    when(accountExistsValidator.validate(accountId)).thenReturn(new ValidationResponse(true, ""));
    when(transactionTotalValidator.validate(10L, accountId))
        .thenReturn(new ValidationResponse(true, ""));
    when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
    when(transactionProxyService.createTransactionForAccount(accountId, 10L))
        .thenReturn(transaction);

    final Transaction result = transactionService.createTransactionForAccount(accountId, 10L);

    account.setBalance(10L);

    verify(accountRepository).save(account);
    assertThat(result, is(transaction));
  }

  @Test
  void getAccountTransactionsShouldReturnTransactionsForAccount() {
    final List<Transaction> transactionList =
        List.of(getTransaction(), getTransaction(), getTransaction());

    when(transactionProxyService.getAccountTransactions(accountId)).thenReturn(transactionList);

    final List<Transaction> result = transactionService.getAccountTransactions(accountId);

    assertThat(result, is(transactionList));
  }

  private Transaction getTransaction() {
    return new Transaction(UUID.randomUUID(), Instant.now(), accountId, 0L);
  }
}
