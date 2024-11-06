package com.example.transaction.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.transaction.model.Transaction;
import com.example.transaction.service.TransactionService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerIntegrationTest {

  @Autowired private TransactionService transactionService;

  @Autowired private MockMvc mockMvc;

  private UUID accountId;

  @BeforeEach
  void setUp() {
    accountId = UUID.randomUUID();
  }

  @Test
  void shouldRejectUnauthorisedRequests() throws Exception {
    mockMvc
        .perform(
            post("/accounts/" + accountId + "/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("10"))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
  }

  @Test
  void createTransactionShouldCreateAndReturnTransaction() throws Exception {
    mockMvc
        .perform(
            post("/accounts/" + accountId + "/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("10")
                .with(httpBasic("test-user", "test-password")))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.createdAt").isNotEmpty())
        .andExpect(jsonPath("$.accountId").value(accountId.toString()))
        .andExpect(jsonPath("$.amount").value("10"));
  }

  @Test
  void getAllTransactionsForAccountShouldReturnTransactions() throws Exception {
    final Transaction transaction = transactionService.createTransactionForAccount(accountId, 10L);

    mockMvc
        .perform(
            get("/accounts/" + accountId + "/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(httpBasic("test-user", "test-password")))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
        .andExpect(jsonPath("$[0].accountId").value(accountId.toString()))
        .andExpect(jsonPath("$[0].amount").value("10"));
  }
}
