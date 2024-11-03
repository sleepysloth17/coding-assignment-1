package com.example.account.service;

import com.example.account.model.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  public Customer createCustomer(String name, String surname) {
    return null; // TODO
  }

  public Optional<Customer> getCustomer(UUID customerId) {
    return Optional.empty(); // TODO
  }

  public List<Customer> getCustomers() {
    return List.of(); // TODO
  }
}
