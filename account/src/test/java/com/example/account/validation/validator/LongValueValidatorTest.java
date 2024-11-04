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

class LongValueValidatorTest {

  private LongValueValidator longValueValidator;

  private final long min = 0L;
  private final long max = 10L;

  @BeforeEach
  void setUp() {
    longValueValidator = new LongValueValidator(min, max);
  }

  @Test
  void shouldDefaultToMinAndMaxLongValues() {
    final LongValueValidator defaultValidator = new LongValueValidator(null, null);

    assertAll(
        () -> assertTrue(defaultValidator.validate(Long.MAX_VALUE).isValid()),
        () -> assertTrue(defaultValidator.validate(Long.MIN_VALUE).isValid()));
  }

  @Test
  void shouldReturnValidResponseIfValueInRange() {
    assertAll(
        () -> assertTrue(longValueValidator.validate(max).isValid()),
        () -> assertTrue(longValueValidator.validate(min).isValid()),
        () -> assertTrue(longValueValidator.validate(min + 1).isValid()),
        () -> assertTrue(longValueValidator.validate(max - 1).isValid()));
  }

  @Test
  void shouldReturnInvalidResponseIfValueNotInRange() {
    assertAll(
        () -> assertFalse(longValueValidator.validate(max + 1).isValid()),
        () ->
            assertThat(
                longValueValidator.validate(max + 1).getMessages(),
                is(Collections.singletonList("Expected value in range [0, 10] but got 11"))),
        () -> assertFalse(longValueValidator.validate(min - 1).isValid()),
        () ->
            assertThat(
                longValueValidator.validate(min - 1).getMessages(),
                is(Collections.singletonList("Expected value in range [0, 10] but got -1"))));
  }

  @Test
  void shouldReturnInvalidResponseIfValueIsNull() {
    final ValidationResponse response = longValueValidator.validate(null);

    assertFalse(response.isValid());
    assertThat(
        response.getMessages(),
        is(Collections.singletonList("Expected value in range [0, 10] but got null value")));
  }
}
