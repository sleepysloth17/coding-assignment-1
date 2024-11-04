package com.example.account.validation;

import java.util.function.Supplier;

public class ValidationRunner {

  public static <T> ValidationRunner from(Validator<T> validator, T value) {
    return new ValidationRunner(validator.validate(value));
  }

  public static <T, U> ValidationRunner from(CombinedValidator<T, U> validator, T tValue, U uValue) {
    return new ValidationRunner(validator.validate(tValue, uValue));
  }

  private final ValidationResponse validationResponse;

  private ValidationRunner(ValidationResponse validationResponse) {
    this.validationResponse = validationResponse;
  }

  public <T> ValidationRunner and(Validator<T> validator, T value) {
    return new ValidationRunner(this.validationResponse.and(validator.validate(value)));
  }

  public <T, U> ValidationRunner and(CombinedValidator<T, U> validator, T tValue, U uValue) {
    return new ValidationRunner(this.validationResponse.and(validator.validate(tValue, uValue)));
  }

  public <U> U ifValidOrThrow(Supplier<U> action) throws ValidationException {
    if (validationResponse.isValid()) {
      return action.get();
    }

    throw new ValidationException(validationResponse.getMessages());
  }
}
