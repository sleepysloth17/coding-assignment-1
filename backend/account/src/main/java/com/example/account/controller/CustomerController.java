package com.example.account.controller;

import com.example.account.dto.Assembler;
import com.example.account.dto.CustomerDto;
import com.example.account.service.CustomerService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

  private final CustomerService customerService;

  private final Assembler customerAssembler;

  public CustomerController(CustomerService customerService, Assembler assembler) {
    this.customerService = customerService;
    this.customerAssembler = assembler;
  }

  @PostMapping(value = "customers")
  public ResponseEntity<CustomerDto> createCustomer(
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "surname", required = true) String surname) {
    return ResponseEntity.ok(customerService.createCustomer(name, surname));
  }

  @GetMapping(value = "customers")
  public ResponseEntity<List<CustomerDto>> getCustomers() {
    return ResponseEntity.ok(
        customerService.getCustomers().stream()
            .map(c -> customerAssembler.getCustomerDto(c.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList());
  }
}
