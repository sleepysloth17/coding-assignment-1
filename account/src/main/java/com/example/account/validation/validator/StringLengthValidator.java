package com.example.account.validation.validator;

import com.example.account.validation.ValidationResponse;
import com.example.account.validation.Validator;

public class StringLengthValidator implements Validator<String> {

  private static final int DEFAULT_MAX = 200;

  private static final String INVALID_LENGTH_MESSAGE_TEMPLATE =
      "Expected string length in range [%d, %d] but has length %d";

  private static final String NULL_STRING_MESSAGE_TEMPLATE =
      "Expected string length in range [%d, %d] but string is null";

  private final int minLength;

  private final int maxLength;

  public StringLengthValidator(Integer minLength, Integer maxLength) {
    this.minLength = minLength == null ? 0 : minLength;
    this.maxLength = maxLength == null ? DEFAULT_MAX : maxLength;
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
