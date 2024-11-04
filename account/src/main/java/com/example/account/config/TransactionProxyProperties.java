package com.example.account.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "transaction-service")
public class TransactionProxyProperties {

  private String url;
}
