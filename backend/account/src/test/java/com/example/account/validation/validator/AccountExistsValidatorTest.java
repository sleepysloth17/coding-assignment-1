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
class AccountExistsValidatorTest {

  @InjectMocks private AccountExistsValidator accountExistsValidator;

  @Mock private AccountRepository accountRepository;

  private Account account;

  @BeforeEach
  void setUp() {
    account = new Account();
    account.setId(UUID.randomUUID());
  }

  @Test
  void shouldReturnValidResponseIfUserAccountWithGivenIdExists() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    final ValidationResponse response = accountExistsValidator.validate(account.getId());

    assertTrue(response.isValid());
    assertThat(response.getMessages(), is(Collections.emptyList()));
  }

  @Test
  void shouldReturnInvalidResponseIfNoUserAccountWithGivenIdExists() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.empty());

    final ValidationResponse response = accountExistsValidator.validate(account.getId());

    assertFalse(response.isValid());
    assertThat(
        response.getMessages(),
        is(Collections.singletonList("Account does not exist with id: " + account.getId())));
  }
}
