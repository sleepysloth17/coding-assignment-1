package com.example.account.validation.validator;

import com.example.account.validation.ValidationResponse;
import com.example.account.validation.Validator;

public class StringLengthValidator implements Validator<String> {

  private static final String INVALID_LENGTH_MESSAGE_TEMPLATE =
      "Expected string length of range [%d, %d] but has length %d";

  private static final String NULL_STRING_MESSAGE_TEMPLATE =
      "Expected string length of range [%d, %d] but has null length";

  private final long minLength;

  private final long maxLength;

  public StringLengthValidator(Long minLength, Long maxLength) {
    this.minLength = minLength == null ? 0L : minLength;
    this.maxLength = maxLength == null ? Long.MAX_VALUE : maxLength;
  }

  @Override
  public ValidationResponse validate(String str) {
    if (str == null) {
      return new ValidationResponse(
          false, String.format(NULL_STRING_MESSAGE_TEMPLATE, minLength, maxLength));
    }

    return new ValidationResponse(
        str.length() >= minLength && str.length() <= maxLength,
        String.format(INVALID_LENGTH_MESSAGE_TEMPLATE, minLength, maxLength, str.length()));
  }
}
