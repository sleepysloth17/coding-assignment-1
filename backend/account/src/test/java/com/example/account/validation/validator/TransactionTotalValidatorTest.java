package com.example.account.validation.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.account.model.Account;
import com.example.account.repository.AccountRepository;
import com.example.account.validation.ValidationResponse;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionTotalValidatorTest {

  @InjectMocks private TransactionTotalValidator transactionTotalValidator;

  @Mock private AccountRepository accountRepository;

  private UUID accountId;

  @BeforeEach
  void setUp() {
    accountId = UUID.randomUUID();
  }

  @Test
  void shouldReturnValidIfAmountGreaterThanZero() {
    final ValidationResponse response = transactionTotalValidator.validate(100L, accountId);

    assertTrue(response.isValid());
  }

  @Test
  void shouldReturnInvalidIfAmountIsNull() {
    final ValidationResponse response = transactionTotalValidator.validate(null, accountId);

    assertFalse(response.isValid());
    assertThat(
        response.getMessages(), is(Collections.singletonList("Invalid transaction amount: null")));
  }

  @Test
  void shouldReturnValidIfAmountLessThanZeroAndEnoughInAccount() {
    when(accountRepository.findById(accountId))
        .thenReturn(Optional.of(new Account(accountId, UUID.randomUUID(), 10L)));

    final ValidationResponse response = transactionTotalValidator.validate(-5L, accountId);

    assertTrue(response.isValid());
  }

  @Test
  void shouldReturnInvalidIfAmountLessThanZeroAndNotEnoughInAccount() {
    when(accountRepository.findById(accountId))
        .thenReturn(Optional.of(new Account(accountId, UUID.randomUUID(), 10L)));

    final ValidationResponse response = transactionTotalValidator.validate(-15L, accountId);

    assertFalse(response.isValid());
    assertThat(
        response.getMessages(),
        is(
            Collections.singletonList(
                "Account with id " + accountId + " has insufficient funds to withdraw " + 15L)));
  }
}
