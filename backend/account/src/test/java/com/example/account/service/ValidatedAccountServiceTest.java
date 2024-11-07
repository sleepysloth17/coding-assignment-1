package com.example.account.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
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
import com.example.account.validation.validator.CustomerExistsValidator;
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
class ValidatedAccountServiceTest {

  @InjectMocks private ValidatedAccountService validatedAccountService;

  @Mock private AccountRepository accountRepository;

  @Mock private TransactionProxyService transactionProxyService;

  @Mock private CustomerExistsValidator customerExistsValidator;

  private Account account;

  @BeforeEach
  void setUp() {
    account = getAccount(UUID.randomUUID());
  }

  @Test
  void createAccountShouldNotCreateAccountWithNegativeInitialValue() {
    final UUID customerId = UUID.randomUUID();

    when(customerExistsValidator.validate(customerId)).thenReturn(new ValidationResponse(true, ""));

    assertThrows(ValidationException.class, () -> validatedAccountService.createAccount(customerId, -10L));

    verify(accountRepository, never()).save(any());
  }

  @Test
  void createAccountShouldNotCreateAccountWithNullInitialValue() {
    final UUID customerId = UUID.randomUUID();

    when(customerExistsValidator.validate(customerId))
        .thenReturn(new ValidationResponse(true, null));

    assertThrows(ValidationException.class, () -> validatedAccountService.createAccount(customerId, -10L));

    verify(accountRepository, never()).save(any());
  }

  @Test
  void createAccountShouldNotCreateAccountWithNonExistingCustomer() {
    final UUID customerId = UUID.randomUUID();

    when(customerExistsValidator.validate(customerId))
        .thenReturn(new ValidationResponse(false, ""));

    assertThrows(ValidationException.class, () -> validatedAccountService.createAccount(customerId, 10L));

    verify(accountRepository, never()).save(any());
  }

  @Test
  void createAccountShouldCreateAccountWithExistingCustomerAndZeroInitialValue() {
    final UUID customerId = UUID.randomUUID();

    when(customerExistsValidator.validate(customerId)).thenReturn(new ValidationResponse(true, ""));
    when(accountRepository.save(any())).then(returnsFirstArg());

    final Account created = validatedAccountService.createAccount(customerId, 0L);

    assertNotNull(created);
    assertThat(created.getCustomerId(), is(customerId));
  }

  @Test
  void createAccountShouldCreateAccountAndInitialTransaction() {
    final UUID customerId = UUID.randomUUID();
    final UUID accountId = UUID.randomUUID();

    when(customerExistsValidator.validate(customerId)).thenReturn(new ValidationResponse(true, ""));
    when(accountRepository.save(any()))
        .then(
            i -> {
              final Account account = (Account) i.getArguments()[0];
              account.setId(accountId);
              ;
              return account;
            });
    when(transactionProxyService.createTransactionForAccount(accountId, 10L))
        .thenReturn(new Transaction(null, null, null, 10L));

    final Account created = validatedAccountService.createAccount(customerId, 10L);

    assertNotNull(created);
    assertThat(created.getCustomerId(), is(customerId));
  }

  @Test
  void createAccountShouldDeleteAccountOnTransactionCreationFailure() {
    final UUID customerId = UUID.randomUUID();

    final Account account = new Account();

    when(customerExistsValidator.validate(customerId)).thenReturn(new ValidationResponse(true, ""));
    when(accountRepository.save(any())).thenReturn(account);
    when(transactionProxyService.createTransactionForAccount(any(), anyLong())).thenReturn(null);
    when(accountRepository.findById(any())).thenReturn(Optional.of(account));

    final Exception exception =
        assertThrows(
            IllegalStateException.class, () -> validatedAccountService.createAccount(customerId, 10L));

    assertThat(
        exception.getMessage(),
        is("Failed to create account: failed to create initial transaction"));
    verify(accountRepository).delete(any());
  }

  @Test
  void deleteAccountShouldDeleteAndReturnDeletedAccountIfItExists() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    final Optional<Account> result = validatedAccountService.deleteAccount(account.getId());

    assertTrue(result.isPresent());
    assertThat(result.get(), is(account));
    verify(accountRepository).delete(account);
  }

  @Test
  void deleteAccountShouldReturnAndDeleteNothingIfNoAccountExists() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.empty());

    final Optional<Account> result = validatedAccountService.deleteAccount(account.getId());

    assertTrue(result.isEmpty());
    verify(accountRepository, never()).delete(any());
  }

  @Test
  void getAccountShouldReturnTheCorrectAccount() {
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    final Optional<Account> result = validatedAccountService.getAccount(account.getId());

    assertTrue(result.isPresent());
    assertThat(result.get(), is(account));
  }

  @Test
  void getCustomerAccountsShouldReturnTheListOfMatchingAccounts() {
    final UUID customerId = UUID.randomUUID();
    final List<Account> accountList =
        List.of(getAccount(customerId), getAccount(customerId), getAccount(customerId));

    when(accountRepository.findByCustomerId(customerId)).thenReturn(accountList);

    final List<Account> result = validatedAccountService.getCustomerAccounts(customerId);

    assertThat(result, is(accountList));
  }

  private Account getAccount(UUID customerId) {
    final Account a = new Account();
    a.setId(UUID.randomUUID());
    a.setCustomerId(customerId);
    a.setBalance(0L);
    return a;
  }
}
