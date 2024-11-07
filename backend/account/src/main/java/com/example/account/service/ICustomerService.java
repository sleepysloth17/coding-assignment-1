package com.example.account.service;

import com.example.account.dto.CustomerDto;
import com.example.account.model.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICustomerService {
  CustomerDto createCustomer(String name, String surname);

  Optional<Customer> getCustomer(UUID customerId);

  List<Customer> getCustomers();
}
