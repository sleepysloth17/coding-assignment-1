package com.example.account.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class ValidationResponseTest {

  @Test
  void shouldNotAddErrorToMessageListIfValid() {
    assertThat(new ValidationResponse(true, "message").getMessages(), is(Collections.emptyList()));
  }

  @Test
  void shouldAddErrorToMessageListIfInvalid() {
    assertThat(
        new ValidationResponse(false, "message").getMessages(),
        is(Collections.singletonList("message")));
  }

  @Test
  void andShouldBeValidIfNeitherAtInvalid() {
    final ValidationResponse response1 = new ValidationResponse(true, "");
    final ValidationResponse response2 = new ValidationResponse(true, "");

    assertAll(
        () -> assertTrue(response1.and(response2).isValid()),
        () -> assertTrue(response2.and(response1).isValid()));
  }

  @Test
  void andShouldBeInvalidIfOneIsInvalid() {
    final ValidationResponse response1 = new ValidationResponse(false, "");
    final ValidationResponse response2 = new ValidationResponse(true, "");

    assertAll(
        () -> assertFalse(response1.and(response2).isValid()),
        () -> assertFalse(response2.and(response1).isValid()));
  }

  @Test
  void andShouldBeInvalidIfBothAreInvalid() {
    final ValidationResponse response1 = new ValidationResponse(false, "");
    final ValidationResponse response2 = new ValidationResponse(false, "");

    assertAll(
        () -> assertFalse(response1.and(response2).isValid()),
        () -> assertFalse(response2.and(response1).isValid()));
  }

  @Test
  void andShouldCombineErrorMessages() {
    final ValidationResponse response1 = new ValidationResponse(false, "error message 1");
    final ValidationResponse response2 = new ValidationResponse(false, "error message 2");

    assertThat(
        response1.and(response2).getMessages(), is(List.of("error message 1", "error message 2")));
  }
}
