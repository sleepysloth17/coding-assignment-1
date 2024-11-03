package com.example.account.service;

import com.example.account.model.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.account.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Customer createCustomer(String name, String surname) {
    // TODO validate and save customer
    return customerRepository.save(new Customer(null, name, surname));
  }

  public Optional<Customer> deleteCustomer(UUID customerId) {
    final Optional<Customer> customer = getCustomer(customerId);
    customer.ifPresent(customerRepository::delete);
    return customer;
  }

  public Optional<Customer> getCustomer(UUID customerId) {
    return customerRepository.findById(customerId);
  }

  public List<Customer> getCustomers() {
    return customerRepository.findAll();
  }
}
