package com.example.account.validator;

public class StringLengthValidator implements Validator<String> {

  private final long minLength;

  private final long maxLength;

  public StringLengthValidator(Long minLength, Long maxLength) {
    this.minLength = minLength == null ? 0L : minLength;
    this.maxLength = maxLength == null ? Long.MAX_VALUE : maxLength;
  }

  @Override
  public boolean isValid(String str) {
    return str != null && str.length() >= minLength && str.length() <= maxLength;
  }
}
