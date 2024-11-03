package com.example.account.validator;

import com.example.account.repository.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerExistsValidator implements Validator<UUID> {

  private final CustomerRepository customerRepository;

  public CustomerExistsValidator(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public boolean isValid(UUID customerId) {
    return customerRepository.findById(customerId).isPresent();
  }
}
