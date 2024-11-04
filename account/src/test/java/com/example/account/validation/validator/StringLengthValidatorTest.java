package com.example.account.validation.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.account.validation.ValidationResponse;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringLengthValidatorTest {

  private StringLengthValidator stringLengthValidator;

  private final int minLength = 5;
  private final int maxLength = 10;

  @BeforeEach
  void setUp() {
    stringLengthValidator = new StringLengthValidator(minLength, maxLength);
  }

  @Test
  void shouldDefaultToMinLength0() {
    final StringLengthValidator defaultValidator = new StringLengthValidator(null, null);

    assertTrue(defaultValidator.validate(getStringOfLength(0)).isValid());
  }

  @Test
  void shouldDefaultToMaxLength200() {
    final StringLengthValidator defaultValidator = new StringLengthValidator(null, null);

    assertTrue(defaultValidator.validate(getStringOfLength(200)).isValid());
  }

  @Test
  void shouldReturnValidResponseIfLengthInRange() {
    assertAll(
        () -> assertTrue(stringLengthValidator.validate(getStringOfLength(maxLength)).isValid()),
        () -> assertTrue(stringLengthValidator.validate(getStringOfLength(minLength)).isValid()),
        () ->
            assertTrue(stringLengthValidator.validate(getStringOfLength(minLength + 1)).isValid()),
        () ->
            assertTrue(stringLengthValidator.validate(getStringOfLength(maxLength - 1)).isValid()));
  }

  @Test
  void shouldReturnInvalidResponseIfLengthNotInRange() {
    assertAll(
        () ->
            assertFalse(stringLengthValidator.validate(getStringOfLength(maxLength + 1)).isValid()),
        () ->
            assertThat(
                stringLengthValidator.validate(getStringOfLength(maxLength + 1)).getMessages(),
                is(
                    Collections.singletonList(
                        "Expected string length in range [5, 10] but has length 11"))),
        () ->
            assertFalse(stringLengthValidator.validate(getStringOfLength(minLength - 1)).isValid()),
        () ->
            assertThat(
                stringLengthValidator.validate(getStringOfLength(minLength - 1)).getMessages(),
                is(
                    Collections.singletonList(
                        "Expected string length in range [5, 10] but has length 4"))));
  }

  @Test
  void shouldReturnInvalidResponseIfValueIsNull() {
    final ValidationResponse response = stringLengthValidator.validate(null);

    assertFalse(response.isValid());
    assertThat(
        response.getMessages(),
        is(
            Collections.singletonList(
                "Expected string length in range [5, 10] but string is null")));
  }

  private String getStringOfLength(int length) {
    return "x".repeat(length);
  }
}
