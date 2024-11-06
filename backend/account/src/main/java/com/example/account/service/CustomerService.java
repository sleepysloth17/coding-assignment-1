package com.example.account.service;

import com.example.account.model.Customer;
import com.example.account.repository.CustomerRepository;
import com.example.account.validation.ValidationRunner;
import com.example.account.validation.validator.StringLengthValidator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  private final StringLengthValidator nameLengthValidator;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
    this.nameLengthValidator = new StringLengthValidator(1, null);
  }

  public Customer createCustomer(String name, String surname) {
    return ValidationRunner.from(nameLengthValidator, name)
        .and(nameLengthValidator, surname)
        .ifValidOrThrow(
            () -> {
              final Customer customer = new Customer();
              customer.setName(name);
              customer.setSurname(surname);
              return customerRepository.save(customer);
            });
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
