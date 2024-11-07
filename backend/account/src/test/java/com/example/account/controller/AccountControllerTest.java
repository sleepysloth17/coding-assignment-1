package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.example.account.dto.AccountDto;
import com.example.account.service.AccountService;
import com.example.account.service.CustomerService;
import java.util.Collections;
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
    final Long initialValue = 1234L;
    final AccountDto account =
        new AccountDto(UUID.randomUUID(), UUID.randomUUID(), 0L, Collections.emptyList());

    when(accountService.createAccount(customerId, initialValue)).thenReturn(account);

    final ResponseEntity<AccountDto> response =
        accountController.createCustomerAccount(customerId, initialValue);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(account));
  }
}
