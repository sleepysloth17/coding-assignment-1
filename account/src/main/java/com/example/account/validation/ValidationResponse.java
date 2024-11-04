package com.example.account.validation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ValidationResponse {

  private final boolean valid;

  private final List<String> messages;

  public ValidationResponse(boolean valid, String invalidMessage) {
    this(valid, valid ? Collections.emptyList() : List.of(invalidMessage));
  }

  ValidationResponse(boolean valid, List<String> messages) {
    this.valid = valid;
    this.messages = messages;
  }

  public ValidationResponse and(ValidationResponse other) {
    return new ValidationResponse(
        this.valid && other.valid,
        Stream.concat(this.messages.stream(), other.messages.stream()).toList());
  }

  public boolean isValid() {
    return valid;
  }

  public List<String> getMessages() {
    return messages;
  }
}
