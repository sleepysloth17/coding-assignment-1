package com.example.account.validation.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.account.model.Customer;
import com.example.account.repository.CustomerRepository;
import com.example.account.validation.ValidationResponse;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerExistsValidatorTest {

  @InjectMocks private CustomerExistsValidator customerExistsValidator;

  @Mock private CustomerRepository customerRepository;

  private Customer customer;

  @BeforeEach
  void setUp() {
    customer = new Customer();
    customer.setId(UUID.randomUUID());
  }

  @Test
  void shouldReturnValidResponseIfUserAccountWithGivenIdExists() {
    when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

    final ValidationResponse response = customerExistsValidator.validate(customer.getId());

    assertTrue(response.isValid());
    assertThat(response.getMessages(), is(Collections.emptyList()));
  }

  @Test
  void shouldReturnInvalidResponseIfNoUserAccountWithGivenIdExists() {
    when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

    final ValidationResponse response = customerExistsValidator.validate(customer.getId());

    assertFalse(response.isValid());
    assertThat(
        response.getMessages(),
        is(Collections.singletonList("Customer does not exist with id: " + customer.getId())));
  }
}
