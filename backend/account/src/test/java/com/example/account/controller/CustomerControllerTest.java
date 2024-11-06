package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.example.account.model.Customer;
import com.example.account.service.CustomerService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

  @InjectMocks private CustomerController customerController;

  @Mock private CustomerService customerService;

  private UUID customerId;

  @BeforeEach
  void setUp() {
    customerId = UUID.randomUUID();
  }

  @Test
  void createCustomerShouldReturnCreatedCustomer() {
    final String name = "name";
    final String surname = "surname";

    final Customer customer = getCustomer(customerId);

    when(customerService.createCustomer(name, surname)).thenReturn(customer);

    final ResponseEntity<Customer> response = customerController.createCustomer(name, surname);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(customer));
  }

  @Test
  void getCustomersShouldReturnListOfCustomers() {
    final List<Customer> customerList =
        List.of(
            getCustomer(UUID.randomUUID()),
            getCustomer(UUID.randomUUID()),
            getCustomer(UUID.randomUUID()));

    when(customerService.getCustomers()).thenReturn(customerList);

    final ResponseEntity<List<Customer>> response = customerController.getCustomers();

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(customerList));
  }

  @Test
  void getCustomerWithIdShouldReturnCustomerIfExists() {
    final Customer customer = getCustomer(customerId);

    when(customerService.getCustomer(customerId)).thenReturn(Optional.of(customer));

    final ResponseEntity<Customer> response = customerController.getCustomerWithId(customerId);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(customer));
  }

  @Test
  void getCustomerWithIdShouldReturn404IfNotCustomerExistsWithTheGivenId() {

    when(customerService.getCustomer(customerId)).thenReturn(Optional.empty());

    final ResponseEntity<Customer> response = customerController.getCustomerWithId(customerId);

    assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  private Customer getCustomer(UUID id) {
    final Customer customer = new Customer();
    customer.setId(id);
    customer.setName("");
    customer.setSurname("");
    return customer;
  }
}
