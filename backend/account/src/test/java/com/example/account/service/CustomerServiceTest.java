package com.example.account.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.account.model.Customer;
import com.example.account.repository.CustomerRepository;
import com.example.account.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @InjectMocks private CustomerService customerService;

  @Mock private CustomerRepository customerRepository;

  private Customer customer;

  @BeforeEach
  void setUp() {
    customer = new Customer(UUID.randomUUID(), "name", "surname");
  }

  @Test
  void createCustomerShouldNotCreateCustomersWithEmptyFirstName() {
    assertAll(
        () ->
            assertThrows(
                ValidationException.class, () -> customerService.createCustomer("", "surname")),
        () ->
            assertThrows(
                ValidationException.class, () -> customerService.createCustomer(null, "surname")));

    verify(customerRepository, never()).save(any());
  }

  @Test
  void createCustomerShouldNotCreateCustomersWithEmptySurname() {
    assertAll(
        () ->
            assertThrows(
                ValidationException.class, () -> customerService.createCustomer("name", "")),
        () ->
            assertThrows(
                ValidationException.class, () -> customerService.createCustomer("name", null)));
    ;

    verify(customerRepository, never()).save(any());
  }

  @Test
  void createCustomerShouldCreateCustomersWithNonEmptyFirstAndSurnames() {
    when(customerRepository.save(any())).then(returnsFirstArg());

    final Customer created = customerService.createCustomer("name", "surname");

    assertNotNull(created);
    assertThat(created.getName(), is("name"));
    assertThat(created.getSurname(), is("surname"));
  }

  @Test
  void deleteCustomerShouldDeleteAndReturnDeletedCustomerIfItExists() {
    when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

    final Optional<Customer> result = customerService.deleteCustomer(customer.getId());

    assertTrue(result.isPresent());
    assertThat(result.get(), is(customer));
    verify(customerRepository).delete(customer);
  }

  @Test
  void deleteCustomerShouldReturnAndDeleteNothingIfNoCustomerExists() {
    when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

    final Optional<Customer> result = customerService.deleteCustomer(customer.getId());

    assertTrue(result.isEmpty());
    verify(customerRepository, never()).delete(any());
  }

  @Test
  void getCustomerShouldReturnTheCorrectCustomer() {
    when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

    final Optional<Customer> result = customerService.getCustomer(customer.getId());

    assertTrue(result.isPresent());
    assertThat(result.get(), is(customer));
  }

  @Test
  void getCustomersShouldReturnTheListOfMatchingCustomers() {
    final List<Customer> customerList =
        List.of(
            new Customer(UUID.randomUUID(), "", ""),
            new Customer(UUID.randomUUID(), "", ""),
            new Customer(UUID.randomUUID(), "", ""));

    when(customerRepository.findAll()).thenReturn(customerList);

    final List<Customer> result = customerService.getCustomers();

    assertThat(result, is(customerList));
  }
}
