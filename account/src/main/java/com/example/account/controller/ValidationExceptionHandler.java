package com.example.account.controller;

import com.example.account.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ValidationExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Map<String, Object>> handleValidationFailure(ValidationException ex) {
    final Map<String, Object> body = new HashMap<>();
    body.put("error", ex.getMessage());
    body.put("validationErrors", ex.getValidationErrors());
    return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
