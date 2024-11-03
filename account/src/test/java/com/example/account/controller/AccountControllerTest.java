package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.example.account.model.Account;
import com.example.account.model.Customer;
import com.example.account.service.AccountService;
import com.example.account.service.CustomerService;
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
class AccountControllerTest {

  @InjectMocks private AccountController accountController;

  @Mock private AccountService accountService;

  @Mock private CustomerService customerService;

  private UUID customerId;

  @BeforeEach
  void setUp() {
    customerId = UUID.randomUUID();
  }

  @Test
  void createCustomerAccountShouldReturnCreatedAccount() {
    final Account account = new Account(UUID.randomUUID(), customerId);

    when(accountService.createAccount(customerId)).thenReturn(account);

    final ResponseEntity<Account> response = accountController.createCustomerAccount(customerId);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(account));
  }

  @Test
  void getCustomerAccountsShouldReturnListOfCustomerAccountsIfCustomerExists() {
    final List<Account> accountList =
        List.of(
            new Account(UUID.randomUUID(), customerId),
            new Account(UUID.randomUUID(), customerId),
            new Account(UUID.randomUUID(), customerId));

    when(customerService.getCustomer(customerId))
        .thenReturn(Optional.of(new Customer(customerId, "", "")));
    when(accountService.getCustomerAccounts(customerId)).thenReturn(accountList);

    final ResponseEntity<List<Account>> response =
        accountController.getCustomerAccounts(customerId);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(accountList));
  }

  @Test
  void getCustomerAccountsShouldReturn404IfCustomerDoesNotExist() {
    when(customerService.getCustomer(customerId)).thenReturn(Optional.empty());

    final ResponseEntity<List<Account>> response =
        accountController.getCustomerAccounts(customerId);

    assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void getAccountWithIdShouldReturnTheAccountIfOneExists() {
    final UUID accountId = UUID.randomUUID();
    final Account account = new Account(accountId, customerId);

    when(accountService.getAccount(accountId)).thenReturn(Optional.of(account));

    final ResponseEntity<Account> response = accountController.getAccountWithId(accountId);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(account));
  }

  @Test
  void getAccountWithIdShouldReturn404IfAccountDoesNotExist() {
    final UUID accountId = UUID.randomUUID();

    when(accountService.getAccount(accountId)).thenReturn(Optional.empty());

    final ResponseEntity<Account> response = accountController.getAccountWithId(accountId);

    assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}
