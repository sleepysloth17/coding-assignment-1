package com.example.account.validation.validator;

import com.example.account.repository.AccountRepository;
import com.example.account.validation.ValidationResponse;
import com.example.account.validation.Validator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AccountExistsValidator implements Validator<UUID> {

  private static final String INVALID_MESSAGE_TEMPLATE = "Account does not exist with id: %s";

  private final AccountRepository accountRepository;

  public AccountExistsValidator(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public ValidationResponse validate(UUID accountId) {
    return new ValidationResponse(
        accountRepository.findById(accountId).isPresent(),
        String.format(INVALID_MESSAGE_TEMPLATE, accountId));
  }
}
