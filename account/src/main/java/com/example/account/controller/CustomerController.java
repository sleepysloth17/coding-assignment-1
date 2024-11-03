package com.example.account.controller;

import com.example.account.model.Customer;
import com.example.account.service.CustomerService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping(value = "customers")
  public ResponseEntity<Customer> createCustomer(
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "surname", required = true) String surname) {
    return ResponseEntity.ok(customerService.createCustomer(name, surname));
  }

  @GetMapping(value = "customers")
  public ResponseEntity<List<Customer>> getCustomers() {
    return ResponseEntity.ok(customerService.getCustomers());
  }

  @GetMapping(value = "customers/{customerId}")
  public ResponseEntity<Customer> getCustomerWithId(
      @PathVariable(value = "customerId") UUID customerId) {
    return ResponseEntity.of(customerService.getCustomer(customerId));
  }
}
