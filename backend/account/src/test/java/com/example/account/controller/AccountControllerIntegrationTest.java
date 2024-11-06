package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.account.model.Account;
import com.example.account.model.Customer;
import com.example.account.model.Transaction;
import com.example.account.service.AccountService;
import com.example.account.service.CustomerService;
import com.example.account.service.TransactionProxyService;
import com.jayway.jsonpath.JsonPath;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

  @Autowired private AccountController accountController;

  @Autowired private CustomerService customerService;

  @Autowired private AccountService accountService;

  @MockBean private TransactionProxyService transactionProxyService;

  @Autowired private MockMvc mockMvc;

  @Test
  void createCustomerAccountShouldReturn422IfCustomerDoesNotExist() throws Exception {
    final UUID customerId = UUID.randomUUID();

    mockMvc
        .perform(post("/customers/" + customerId + "/accounts").param("initialValue", "0"))
        .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
        .andExpect(jsonPath("$.error").value("Validation Error"))
        .andExpect(
            jsonPath("$.validationErrors[0]")
                .value("Customer does not exist with id: " + customerId));
  }

  @Test
  void createCustomerAccountShouldReturn422IfInitialValueInvalid() throws Exception {
    final Customer customer = customerService.createCustomer("name", "surname");

    mockMvc
        .perform(post("/customers/" + customer.getId() + "/accounts").param("initialValue", "-15"))
        .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
        .andExpect(jsonPath("$.error").value("Validation Error"))
        .andExpect(
            jsonPath("$.validationErrors[0]")
                .value("Expected value in range [0, 9223372036854775807] but got -15"));
  }

  @Test
  void createCustomerAccountShouldReturnCreatedAccountWithDefaultInitialValue() throws Exception {
    final Customer customer = customerService.createCustomer("name", "surname");

    final MvcResult result =
        mockMvc
            .perform(post("/customers/" + customer.getId() + "/accounts"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
            .andExpect(jsonPath("$.balance").value("0"))
            .andReturn();
    final String createdAccountId =
        JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");

    final List<Account> accounts = accountService.getCustomerAccounts(customer.getId());
    assertThat(accounts.size(), is(1));
    assertThat(accounts.get(0).getId().toString(), is(createdAccountId));
  }

  @Test
  void createCustomerAccountShouldReturnCreatedAccountForValidInput() throws Exception {
    when(transactionProxyService.createTransactionForAccount(any(), eq(15L)))
        .thenReturn(new Transaction());

    final Customer customer = customerService.createCustomer("name", "surname");

    final MvcResult result =
        mockMvc
            .perform(
                post("/customers/" + customer.getId() + "/accounts").param("initialValue", "15"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
            .andExpect(jsonPath("$.balance").value("15"))
            .andReturn();
    final String createdAccountId =
        JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");

    final List<Account> accounts = accountService.getCustomerAccounts(customer.getId());
    assertThat(accounts.size(), is(1));
    assertThat(accounts.get(0).getId().toString(), is(createdAccountId));
  }

  @Test
  void getCustomerAccountsShouldReturn404IfCustomerDoesNotExist() throws Exception {
    mockMvc
        .perform(get("/customers/" + UUID.randomUUID() + "/accounts"))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
  }

  @Test
  void getCustomerAccountsShouldReturnAccountsIfCustomerExists() throws Exception {
    when(transactionProxyService.createTransactionForAccount(any(), eq(10L)))
        .thenReturn(new Transaction());

    final Customer customer = customerService.createCustomer("name", "surname");
    final Account account = accountService.createAccount(customer.getId(), 10L);

    mockMvc
        .perform(get("/customers/" + customer.getId() + "/accounts"))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$[0]").isNotEmpty())
        .andExpect(jsonPath("$[0].id").value(account.getId().toString()))
        .andExpect(jsonPath("$[0].customerId").value(customer.getId().toString()))
        .andExpect(jsonPath("$[0].balance").value("10"));
  }

  @Test
  void deleteAccountShouldReturn404IfAccountDoesNotExist() throws Exception {
    mockMvc
        .perform(delete("/accounts/" + UUID.randomUUID()))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
  }

  @Test
  void deleteAccountShouldDeleteAndReturnExistingAccount() throws Exception {
    when(transactionProxyService.createTransactionForAccount(any(), eq(10L)))
        .thenReturn(new Transaction());

    final Customer customer = customerService.createCustomer("name", "surname");
    final Account account = accountService.createAccount(customer.getId(), 10L);

    mockMvc
        .perform(delete("/accounts/" + account.getId()))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.id").value(account.getId().toString()))
        .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
        .andExpect(jsonPath("$.balance").value("10"));
    ;

    final List<Account> accounts = accountService.getCustomerAccounts(customer.getId());
    assertTrue(accounts.isEmpty());
  }

  @Test
  void getAccountShouldReturn404IfAccountDoesNotExist() throws Exception {
    mockMvc
        .perform(get("/accounts/" + UUID.randomUUID()))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
  }

  @Test
  void getAccountShouldReturnAccountIfItExistsExist() throws Exception {
    when(transactionProxyService.createTransactionForAccount(any(), eq(10L)))
        .thenReturn(new Transaction());

    final Customer customer = customerService.createCustomer("name", "surname");
    final Account account = accountService.createAccount(customer.getId(), 10L);

    mockMvc
        .perform(get("/accounts/" + account.getId()))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.id").value(account.getId().toString()))
        .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
        .andExpect(jsonPath("$.balance").value("10"));
  }
}
