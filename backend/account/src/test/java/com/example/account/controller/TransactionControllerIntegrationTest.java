package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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
import java.util.Collections;
import java.util.Optional;
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
class TransactionControllerIntegrationTest {

  @Autowired private CustomerService customerService;

  @Autowired private AccountService accountService;

  @MockBean private TransactionProxyService transactionProxyService;

  @Autowired private MockMvc mockMvc;

  @Test
  void createTransactionForAccountShouldCreateTransactionForValidInput() throws Exception {
    final Account account = createAccount();

    final UUID transactionId = UUID.randomUUID();
    when(transactionProxyService.createTransactionForAccount(account.getId(), 10L))
        .thenReturn(getTransaction(transactionId, account.getId(), 10L));

    final MvcResult result =
        mockMvc
            .perform(post("/accounts/" + account.getId() + "/transactions").param("amount", "10"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.accountId").value(account.getId().toString()))
            .andExpect(jsonPath("$.amount").value("10"))
            .andReturn();
    final String createdTransactionId =
        JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");

    assertThat(createdTransactionId, is(transactionId.toString()));
  }

  @Test
  void createTransactionForAccountShouldUpdateAccountBalanceForValidInput() throws Exception {
    final Account account = createAccount();

    final UUID transactionId = UUID.randomUUID();
    when(transactionProxyService.createTransactionForAccount(account.getId(), 10L))
        .thenReturn(getTransaction(transactionId, account.getId(), 10L));

    mockMvc
        .perform(post("/accounts/" + account.getId() + "/transactions").param("amount", "10"))
        .andExpect(status().is(HttpStatus.OK.value()));

    final Optional<Account> optional = accountService.getAccount(account.getId());
    assertTrue(optional.isPresent());
    assertThat(optional.get().getBalance(), is(10L));
  }

  @Test
  void createTransactionForAccountShouldReturn422ForNonExistentAccount() throws Exception {
    final UUID accountId = UUID.randomUUID();

    mockMvc
        .perform(post("/accounts/" + accountId + "/transactions").param("amount", "10"))
        .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
        .andExpect(jsonPath("$.error").value("Validation Error"))
        .andExpect(
            jsonPath("$.validationErrors[0]")
                .value("Account does not exist with id: " + accountId));
  }

  @Test
  void createTransactionForAccountShouldReturn422ForInvalidAmount() throws Exception {
    final Account account = createAccount();

    mockMvc
        .perform(post("/accounts/" + account.getId() + "/transactions").param("amount", "-10"))
        .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
        .andExpect(jsonPath("$.error").value("Validation Error"))
        .andExpect(
            jsonPath("$.validationErrors[0]")
                .value(
                    "Account with id "
                        + account.getId()
                        + " has insufficient funds to withdraw 10"));
  }

  @Test
  void getAllTransactionsForAccountShouldReturn404IfAccountDoesNotExist() throws Exception {
    mockMvc
        .perform(get("/accounts/" + UUID.randomUUID() + "/transactions"))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
  }

  @Test
  void getAllTransactionsForAccountShouldReturnTransactionsForAccount() throws Exception {
    final Account account = createAccount();
    final Transaction transaction = getTransaction(UUID.randomUUID(), account.getId(), 1L);

    when(transactionProxyService.getAccountTransactions(account.getId()))
        .thenReturn(Collections.singletonList(transaction));

    mockMvc
        .perform(get("/accounts/" + account.getId() + "/transactions"))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$[0].id").value(transaction.getId().toString()))
        .andExpect(jsonPath("$[0].accountId").value(account.getId().toString()))
        .andExpect(jsonPath("$[0].amount").value("1"));
  }

  private Account createAccount() {
    when(transactionProxyService.createTransactionForAccount(any(), eq(0L)))
        .thenReturn(new Transaction());
    final Customer customer = customerService.createCustomer("name", "surname");
    return accountService.createAccount(customer.getId(), 0L);
  }

  private Transaction getTransaction(UUID id, UUID accountId, long amount) {
    final Transaction transaction = new Transaction();
    transaction.setId(id);
    transaction.setAccountId(accountId);
    transaction.setAmount(amount);
    return transaction;
  }
}
