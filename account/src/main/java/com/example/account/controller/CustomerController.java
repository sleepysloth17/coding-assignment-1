package com.example.account.controller;

import com.example.account.model.Customer;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

  @PostMapping(value = "customers")
  public ResponseEntity<Customer> createCustomer(
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "surname", required = true) String surname) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @GetMapping(value = "customers")
  public ResponseEntity<List<Customer>> getCustomers() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @GetMapping(value = "customers/{customerId}")
  public ResponseEntity<List<Customer>> getCustomerWithId(
      @PathVariable(value = "customerId") UUID customerId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
