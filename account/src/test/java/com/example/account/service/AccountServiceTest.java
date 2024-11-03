package com.example.account.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.account.model.Account;
import com.example.account.repository.AccountRepository;
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
class AccountServiceTest {

  @InjectMocks private AccountService accountService;

  @Mock private AccountRepository accountRepository;

  private Account account;

  @BeforeEach
  void setUp() {
    account = new Account(UUID.randomUUID(), UUID.randomUUID());
  }

  @Test
  void deleteAccountShouldDeleteAndReturnDeletedAccountIfItExists() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    final Optional<Account> result = accountService.deleteAccount(account.getId());

    assertTrue(result.isPresent());
    assertThat(result.get(), is(account));
    verify(accountRepository).delete(account);
  }

  @Test
  void deleteAccountShouldReturnAndDeleteNothingIfNoAccountExists() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.empty());

    final Optional<Account> result = accountService.deleteAccount(account.getId());

    assertTrue(result.isEmpty());
    verify(accountRepository, never()).delete(any());
  }

  @Test
  void getAccountShouldReturnTheCorrectAccount() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    final Optional<Account> result = accountService.getAccount(account.getId());

    assertTrue(result.isPresent());
    assertThat(result.get(), is(account));
  }

  @Test
  void getCustomerAccountsShouldReturnTheListOfMatchingAccounts() {
    final UUID customerId = UUID.randomUUID();
    final List<Account> accountList =
        List.of(
            new Account(UUID.randomUUID(), customerId),
            new Account(UUID.randomUUID(), customerId),
            new Account(UUID.randomUUID(), customerId));

    when(accountRepository.findByCustomerId(customerId)).thenReturn(accountList);

    final List<Account> result = accountService.getCustomerAccounts(customerId);

    assertThat(result, is(accountList));
  }
}
