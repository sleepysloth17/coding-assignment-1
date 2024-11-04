package com.example.account.validation.validator;

import com.example.account.repository.CustomerRepository;
import com.example.account.validation.ValidationResponse;
import com.example.account.validation.Validator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CustomerExistsValidator implements Validator<UUID> {

  private static final String INVALID_MESSAGE_TEMPLATE = "Customer does not exist with id: %s";

  private final CustomerRepository customerRepository;

  public CustomerExistsValidator(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public ValidationResponse validate(UUID customerId) {
    return new ValidationResponse(
        customerRepository.findById(customerId).isPresent(),
        String.format(INVALID_MESSAGE_TEMPLATE, customerId));
  }
}
