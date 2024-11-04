package com.example.account.service;

import com.example.account.config.TransactionProxyProperties;
import com.example.account.model.Transaction;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TransactionProxyService {

  private final TransactionProxyProperties transactionProxyProperties;

  private final RestTemplate restTemplate;

  public TransactionProxyService(
      TransactionProxyProperties transactionProxyProperties,
      RestTemplateBuilder restTemplateBuilder) {
    this.transactionProxyProperties = transactionProxyProperties;
    this.restTemplate =
        restTemplateBuilder
            .basicAuthentication(
                transactionProxyProperties.getUsername(), transactionProxyProperties.getPassword())
            .build();
  }

  public Transaction createTransactionForAccount(UUID accountId, long amount) {
    final String uri = build("/accounts/", accountId.toString(), "/transactions");
    return restTemplate.postForObject(uri, amount, Transaction.class);
  }

  public List<Transaction> getAccountTransactions(UUID accountId) {
    final String uri = build("/accounts/", accountId.toString(), "/transactions");
    final ResponseEntity<List<Transaction>> response =
        restTemplate.exchange(
            uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Transaction>>() {});
    return response.getStatusCode().is2xxSuccessful()
        ? response.getBody()
        : Collections.emptyList();
  }

  private String build(String... sections) {
    return Arrays.stream(sections)
        .reduce(
            UriComponentsBuilder.fromHttpUrl(transactionProxyProperties.getUrl()),
            UriComponentsBuilder::path,
            (b1, b2) -> b1)
        .toUriString();
  }
}
