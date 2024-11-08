package com.example.account.service;

import com.example.account.dto.CustomerDto;
import com.example.account.model.Customer;
import com.example.account.repository.CustomerRepository;
import com.example.account.validation.ValidationRunner;
import com.example.account.validation.validator.StringLengthValidator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ValidatedCustomerService implements ICustomerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ValidatedCustomerService.class);

  private final CustomerRepository customerRepository;

  private final StringLengthValidator nameLengthValidator;

  public ValidatedCustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
    this.nameLengthValidator = new StringLengthValidator(1, null);
  }

  @Override
  public CustomerDto createCustomer(String name, String surname) {
    return ValidationRunner.from(nameLengthValidator, name)
        .and(nameLengthValidator, surname)
        .ifValidOrThrow(
            () -> {
              final Customer customer = saveCustomer(name, surname);
              LOGGER.info("Created customer with id: {}", customer.getId());
              return CustomerDto.fromCustomer(customer, Collections.emptyList());
            });
  }

  private Customer saveCustomer(String name, String surname) {
    final Customer customer = new Customer();
    customer.setName(name);
    customer.setSurname(surname);
    return customerRepository.save(customer);
  }

  @Override
  public Optional<Customer> getCustomer(UUID customerId) {
    return customerRepository.findById(customerId);
  }

  @Override
  public List<Customer> getCustomers() {
    return customerRepository.findAll();
  }
}
