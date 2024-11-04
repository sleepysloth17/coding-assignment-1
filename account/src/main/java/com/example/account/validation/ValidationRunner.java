package com.example.account.validation;

public class ValidationRunner {

  public static <T> ValidationRunner from(Validator<T> validator, T value) {
      return new ValidationRunner(validator.validate(value));
  }

  private final ValidationResponse validationResponse;

  ValidationRunner(ValidationResponse validationResponse) {
    this.validationResponse = validationResponse;
  }

  public <T> ValidationRunner and(Validator<T> validator, T value) {
    return new ValidationRunner(this.validationResponse.and(validator.validate(value)));
  }

  public void validate() throws ValidationException {
    if (!validationResponse.isValid()) {
      throw new ValidationException(validationResponse.getMessages());
    }
  }
}
