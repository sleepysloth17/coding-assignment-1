package com.example.account.controller;

import com.example.account.model.Account;
import com.example.account.service.AccountService;
import com.example.account.service.CustomerService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

  public final AccountService accountService;

  public final CustomerService customerService;

  public AccountController(AccountService accountService, CustomerService customerService) {
    this.accountService = accountService;
    this.customerService = customerService;
  }

  @PostMapping(value = "customers/{customerId}/accounts")
  public ResponseEntity<Account> createCustomerAccount(
      @PathVariable(value = "customerId") UUID customerId,
      @RequestParam(value = "initialValue", required = false, defaultValue = "0") Long initialValue
      ) {
    return ResponseEntity.ok(accountService.createAccount(customerId, initialValue));
  }

  @GetMapping(value = "customers/{customerId}/accounts")
  public ResponseEntity<List<Account>> getCustomerAccounts(
      @PathVariable(value = "customerId") UUID customerId) {
    return ResponseEntity.of(
        customerService
            .getCustomer(customerId)
            .map(customer -> accountService.getCustomerAccounts(customer.getId())));
  }

  @DeleteMapping(value = "accounts/{accountId}")
  public ResponseEntity<Account> deleteAccount(@PathVariable(value = "accountId") UUID accountId) {
    return ResponseEntity.of(accountService.deleteAccount(accountId));
  }

  @GetMapping(value = "accounts/{accountId}")
  public ResponseEntity<Account> getAccountWithId(
      @PathVariable(value = "accountId") UUID accountId) {
    return ResponseEntity.of(accountService.getAccount(accountId));
  }
}
