package com.example.account.validation;

import java.util.List;

public class ValidationException extends RuntimeException {

  private static final String MESSAGE = "Validation Error";

  private final List<String> validationErrors;

  public ValidationException(List<String> validationErrors) {
    super(MESSAGE);
    this.validationErrors = validationErrors;
  }

  public List<String> getValidationErrors() {
    return validationErrors;
  }
}
