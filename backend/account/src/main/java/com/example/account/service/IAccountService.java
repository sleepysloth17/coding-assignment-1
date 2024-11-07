package com.example.account.service;

import com.example.account.dto.AccountDto;
import com.example.account.model.Account;
import java.util.List;
import java.util.UUID;

public interface IAccountService {
  AccountDto createAccount(UUID customerId, Long initialValue);

  List<Account> getCustomerAccounts(UUID customerId);
}
