package com.example.account.validation.validator;

import com.example.account.validation.ValidationResponse;
import com.example.account.validation.Validator;

public class LongValueValidator implements Validator<Long> {

  private static final String INVALID_MESSAGE_TEMPLATE =
      "Expected value in range [%d, %d] but got %d";

  private final long min;

  private final long max;

  public LongValueValidator(Long min, Long max) {
    this.min = min == null ? Long.MIN_VALUE : min;
    this.max = max == null ? Long.MAX_VALUE : max;
  }

  @Override
  public ValidationResponse validate(Long aLong) {
    return new ValidationResponse(
        aLong != null && aLong >= min && aLong <= max,
        String.format(INVALID_MESSAGE_TEMPLATE, min, max, aLong));
  }
}
