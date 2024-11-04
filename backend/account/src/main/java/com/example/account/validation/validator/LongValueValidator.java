package com.example.account.validation.validator;

import com.example.account.validation.ValidationResponse;
import com.example.account.validation.Validator;

public class LongValueValidator implements Validator<Long> {
  private static final String INVALID_VALUE_MESSAGE_TEMPLATE =
      "Expected value in range [%d, %d] but got %d";

  private static final String NULL_VALUE_MESSAGE_TEMPLATE =
      "Expected value in range [%d, %d] but got null value";

  private final long min;

  private final long max;

  public LongValueValidator(Long min, Long max) {
    this.min = min == null ? Long.MIN_VALUE : min;
    this.max = max == null ? Long.MAX_VALUE : max;
  }

  @Override
  public ValidationResponse validate(Long aLong) {
    if (aLong == null) {
      return new ValidationResponse(false, String.format(NULL_VALUE_MESSAGE_TEMPLATE, min, max));
    }

    return new ValidationResponse(
        aLong >= min && aLong <= max,
        String.format(INVALID_VALUE_MESSAGE_TEMPLATE, min, max, aLong));
  }
}
