package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.List;
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
class AccountControllerIntegrationTest {

  @Autowired private CustomerRepository customerRepository;

  @Autowired private AccountRepository accountRepository;

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
    final Customer customer = createCustomer();

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
    final Customer customer = createCustomer();

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

    final Optional<Account> account = accountRepository.findById(UUID.fromString(createdAccountId));
    assertTrue(account.isPresent());
    assertThat(account.get().getCustomerId(), is(customer.getId()));
    assertThat(account.get().getBalance(), is(0L));
  }

  @Test
  void createCustomerAccountShouldReturnCreatedAccountForValidInput() throws Exception {
    final UUID transactionId = UUID.randomUUID();
    when(transactionProxyService.createTransactionForAccount(any(), eq(15L)))
        .thenReturn(new Transaction(transactionId, null, null, 15L));

    final Customer customer = createCustomer();

    final MvcResult result =
        mockMvc
            .perform(
                post("/customers/" + customer.getId() + "/accounts").param("initialValue", "15"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
            .andExpect(jsonPath("$.balance").value("15"))
            .andExpect(jsonPath("$.transactions[0].id").value(transactionId.toString()))
            .andExpect(jsonPath("$.transactions[0].amount").value("15"))
            .andReturn();
    final String createdAccountId =
        JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");

    final Optional<Account> account = accountRepository.findById(UUID.fromString(createdAccountId));
    assertTrue(account.isPresent());
    assertThat(account.get().getCustomerId(), is(customer.getId()));
    assertThat(account.get().getBalance(), is(15L));
  }

  @Test
  void createCustomerAccountShouldNotCreateAccountIfTransactionCreationFails() throws Exception {
    when(transactionProxyService.createTransactionForAccount(any(), eq(15L))).thenReturn(null);

    final Customer customer = createCustomer();

    mockMvc
        .perform(post("/customers/" + customer.getId() + "/accounts").param("initialValue", "15"))
        .andExpect(status().is(HttpStatus.OK.value()));

    final List<Account> accounts = accountRepository.findByCustomerId(customer.getId());
    assertTrue(accounts.isEmpty());
  }

  private Customer createCustomer() {
    final Customer customer = new Customer();
    customer.setName("name");
    customer.setSurname("surname");
    return customerRepository.save(customer);
  }
}
