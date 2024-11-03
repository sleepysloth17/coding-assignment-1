package com.example.account.validator;

import com.example.account.repository.AccountRepository;
import java.util.UUID;

public class AccountExistsValidator implements Validator<UUID> {

  private final AccountRepository accountRepository;

  public AccountExistsValidator(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public boolean isValid(UUID accountId) {
    return accountRepository.findById(accountId).isPresent();
  }
}
