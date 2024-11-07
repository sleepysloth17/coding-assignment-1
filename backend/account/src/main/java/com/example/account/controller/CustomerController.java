package com.example.account.controller;

import com.example.account.service.CustomerAssembler;
import com.example.account.dto.CustomerDto;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

  private final CustomerAssembler customerAssembler;

  public CustomerController(CustomerAssembler customerAssembler) {
    this.customerAssembler = customerAssembler;
  }

  @PostMapping(value = "customers")
  public ResponseEntity<CustomerDto> createCustomer(
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "surname", required = true) String surname) {
    return ResponseEntity.ok(customerAssembler.createCustomer(name, surname));
  }

  @GetMapping(value = "customers")
  public ResponseEntity<List<CustomerDto>> getCustomers() {
    return ResponseEntity.ok(customerAssembler.getCustomers());
  }
}
