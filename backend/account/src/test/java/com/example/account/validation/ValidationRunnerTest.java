package com.example.account.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class ValidationRunnerTest {

  @Test
  void shouldNotThrowExceptionForValidInput() {
    final boolean valid =
        ValidationRunner.from(getValidatorWithMessage("value is null"), "str")
            .ifValidOrThrow(() -> true);

    assertTrue(valid);
  }

  @Test
  void shouldThrowExceptionForInvalidInput() {
    final ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                ValidationRunner.from(getValidatorWithMessage("value is null"), null)
                    .ifValidOrThrow(() -> true));

    assertThat(exception.getValidationErrors(), is(List.of("value is null")));
  }

  @Test
  void andShouldNotThrowIfAllValidatorsAreValid() {
    final boolean valid =
        ValidationRunner.from(getValidatorWithMessage("value is null"), "str")
            .and(getValidatorWithMessage("value is null"), "str")
            .and(getValidatorWithMessage("value is null"), "str")
            .ifValidOrThrow(() -> true);

    assertTrue(valid);
  }

  @Test
  void andShouldThrowIfNotAllValidatorsAreValid() {
    assertThrows(
        ValidationException.class,
        () ->
            ValidationRunner.from(getValidatorWithMessage("value is null"), "str")
                .and(getValidatorWithMessage("value is null"), "str")
                .and(getValidatorWithMessage("value is null"), null)
                .ifValidOrThrow(() -> true));
  }

  @Test
  void andShouldCombineErrorMessagesOnInvalidInput() {
    final ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                ValidationRunner.from(getValidatorWithMessage("value a is null"), null)
                    .and(getValidatorWithMessage("value b is null"), null)
                    .and(getValidatorWithMessage("value c is null"), null)
                    .ifValidOrThrow(() -> true));

    assertThat(
        exception.getValidationErrors(),
        is(List.of("value a is null", "value b is null", "value c is null")));
  }

  private TestNullValueValidator getValidatorWithMessage(String message) {
    return new TestNullValueValidator(message);
  }

  private record TestNullValueValidator(String message) implements Validator<String> {
    @Override
    public ValidationResponse validate(String s) {
      return new ValidationResponse(s != null, message);
    }
  }
}
