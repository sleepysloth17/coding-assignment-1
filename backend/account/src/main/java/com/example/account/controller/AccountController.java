package com.example.account.controller;

import com.example.account.dto.AccountDto;
import com.example.account.service.AccountService;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

  public final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping(value = "customers/{customerId}/accounts")
  public ResponseEntity<AccountDto> createCustomerAccount(
      @PathVariable(value = "customerId") UUID customerId,
      @RequestParam(value = "initialValue", required = false, defaultValue = "0")
          Long initialValue) {
    return ResponseEntity.ok(accountService.createAccount(customerId, initialValue));
  }
}
