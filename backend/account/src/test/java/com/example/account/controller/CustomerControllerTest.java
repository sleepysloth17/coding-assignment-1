package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.example.account.dto.CustomerDto;
import com.example.account.service.CustomerAssembler;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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

  @Mock private CustomerAssembler customerAssembler;

  @Test
  void createCustomerShouldReturnCreatedCustomer() {
    final String name = "name";
    final String surname = "surname";

    final CustomerDto customer =
        new CustomerDto(UUID.randomUUID(), "", "", Collections.emptyList());

    when(customerAssembler.createCustomer(name, surname)).thenReturn(customer);

    final ResponseEntity<CustomerDto> response = customerController.createCustomer(name, surname);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(customer));
  }

  @Test
  void getCustomersShouldReturnListOfCustomers() {
    final List<CustomerDto> customerList =
        List.of(
            new CustomerDto(UUID.randomUUID(), "", "", Collections.emptyList()),
            new CustomerDto(UUID.randomUUID(), "", "", Collections.emptyList()),
            new CustomerDto(UUID.randomUUID(), "", "", Collections.emptyList()));

    when(customerAssembler.getCustomers()).thenReturn(customerList);

    final ResponseEntity<List<CustomerDto>> response = customerController.getCustomers();

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(customerList));
  }
}
