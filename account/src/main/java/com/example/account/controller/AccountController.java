package com.example.account.controller;

import com.example.account.model.Account;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

  @PostMapping(value = "customers/{customerId}/accounts")
  public ResponseEntity<Account> createCustomerAccount(
      @PathVariable(value = "customerId") UUID customerId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @GetMapping(value = "customers/{customerId}/accounts")
  public ResponseEntity<List<Account>> getCustomerAccounts(
      @PathVariable(value = "customerId") UUID customerId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @GetMapping(value = "accounts/{accountId}")
  public ResponseEntity<List<Account>> getAccountWithId(
      @PathVariable(value = "accountId") UUID accountId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
