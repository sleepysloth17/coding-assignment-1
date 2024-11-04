package com.example.account.validation;

import java.util.function.Supplier;

public class ValidationRunner {

  public static <T> ValidationRunner from(Validator<T> validator, T value) {
    return new ValidationRunner(validator.validate(value));
  }

  private final ValidationResponse validationResponse;

  private ValidationRunner(ValidationResponse validationResponse) {
    this.validationResponse = validationResponse;
  }

  public <T> ValidationRunner and(Validator<T> validator, T value) {
    return new ValidationRunner(this.validationResponse.and(validator.validate(value)));
  }

  public <U> U ifValidOrThrow(Supplier<U> action) throws ValidationException {
    if (validationResponse.isValid()) {
      return action.get();
    }

    throw new ValidationException(validationResponse.getMessages());
  }
}
