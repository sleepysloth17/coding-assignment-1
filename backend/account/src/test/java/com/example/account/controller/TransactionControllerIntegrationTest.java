package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.account.model.Account;
import com.example.account.model.Customer;
import com.example.account.model.Transaction;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.CustomerRepository;
import com.example.account.service.TransactionProxyService;
import com.jayway.jsonpath.JsonPath;
import java.time.Instant;
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

  @Autowired private CustomerRepository customerRepository;

  @Autowired private AccountRepository accountRepository;

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

    final Optional<Account> optional = accountRepository.findById(account.getId());
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

  private Account createAccount() {
    final Customer customer = createCustomer();
    final Account account = new Account();
    account.setCustomerId(customer.getId());
    account.setBalance(0L);
    return accountRepository.save(account);
  }

  private Customer createCustomer() {
    final Customer customer = new Customer();
    customer.setName("name");
    customer.setSurname("surname");
    return customerRepository.save(customer);
  }

  private Transaction getTransaction(UUID id, UUID accountId, long amount) {
    return new Transaction(id, Instant.now(), accountId, amount);
  }
}
