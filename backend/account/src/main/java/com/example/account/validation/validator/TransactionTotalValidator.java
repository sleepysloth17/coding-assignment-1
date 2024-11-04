package com.example.account.validation.validator;

import com.example.account.repository.AccountRepository;
import com.example.account.validation.CombinedValidator;
import com.example.account.validation.ValidationResponse;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TransactionTotalValidator implements CombinedValidator<Long, UUID> {

  private static final String ACCOUNT_HAS_INSUFFICIENT_FUNDS_MESSAGE =
      "Account with id %s has insufficient funds to withdraw %d";

  private static final String INVALID_TRANSACTION_AMOUNT_MESSAGE =
      "Invalid transaction amount: null";

  private final AccountRepository accountRepository;

  public TransactionTotalValidator(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public ValidationResponse validate(Long amount, UUID accountId) {
    if (amount == null) {
      return new ValidationResponse(false, INVALID_TRANSACTION_AMOUNT_MESSAGE);
    } else if (amount < 0) {
      return new ValidationResponse(
          accountRepository
              .findById(accountId)
              .map(account -> account.getBalance() >= Math.abs(amount))
              .orElse(false),
          String.format(ACCOUNT_HAS_INSUFFICIENT_FUNDS_MESSAGE, accountId, Math.abs(amount)));
    }

    return new ValidationResponse(true, "");
  }
}
