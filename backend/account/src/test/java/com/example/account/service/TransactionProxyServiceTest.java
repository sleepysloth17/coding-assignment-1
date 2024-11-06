package com.example.account.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.account.config.TransactionProxyProperties;
import com.example.account.model.Transaction;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class TransactionProxyServiceTest {

  private static final String MOCK_URL = "http://mock.transaction/api";

  private TransactionProxyService transactionProxyService;

  @Mock private RestTemplateBuilder restTemplateBuilder;

  @Mock private RestTemplate restTemplate;

  private UUID accountId;

  @BeforeEach
  void setUp() {
    final String username = "username";
    final String password = "password";
    accountId = UUID.randomUUID();

    when(restTemplateBuilder.basicAuthentication(username, password))
        .thenReturn(restTemplateBuilder);
    when(restTemplateBuilder.build()).thenReturn(restTemplate);
    transactionProxyService =
        new TransactionProxyService(
            new MockTransactionProperties(username, password), restTemplateBuilder);
  }

  @Test
  void createTransactionForAccountShouldPostToCorrectEndpointAndReturnTransaction() {
    final Transaction transaction = getTransaction(10L);

    when(restTemplate.postForObject(
            MOCK_URL + "/accounts/" + accountId + "/transactions", 10L, Transaction.class))
        .thenReturn(transaction);

    final Transaction result = transactionProxyService.createTransactionForAccount(accountId, 10L);

    assertThat(result, is(transaction));
  }

  @Test
  void getAccountTransactionsShouldGetCorrectEndpointAndReturnTransactions() {
    final List<Transaction> transactionList =
        List.of(getTransaction(1L), getTransaction(2L), getTransaction(3L));

    when(restTemplate.exchange(
            MOCK_URL + "/accounts/" + accountId + "/transactions",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Transaction>>() {}))
        .thenReturn(ResponseEntity.ok(transactionList));

    final List<Transaction> result = transactionProxyService.getAccountTransactions(accountId);

    assertThat(result, is(transactionList));
  }

  @Test
  void getAccountTransactionsShouldReturnEmptyListForUnsuccessfulRequest() {
    when(restTemplate.exchange(
            MOCK_URL + "/accounts/" + accountId + "/transactions",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Transaction>>() {}))
        .thenReturn(ResponseEntity.badRequest().build());

    final List<Transaction> result = transactionProxyService.getAccountTransactions(accountId);

    assertTrue(result.isEmpty());
  }

  private Transaction getTransaction(long amount) {
    final Transaction transaction = new Transaction();
    transaction.setId(UUID.randomUUID());
    transaction.setAccountId(accountId);
    transaction.setAmount(amount);
    return transaction;
  }

  private static class MockTransactionProperties extends TransactionProxyProperties {
    public MockTransactionProperties(String username, String password) {
      this.setUrl(MOCK_URL);
      this.setUsername(username);
      this.setPassword(password);
    }
  }
}
