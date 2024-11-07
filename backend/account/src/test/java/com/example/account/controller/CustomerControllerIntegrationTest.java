package com.example.account.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.account.model.Customer;
import com.example.account.service.ValidatedCustomerService;
import com.jayway.jsonpath.JsonPath;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

  @Autowired private ValidatedCustomerService validatedCustomerService;

  @Autowired private MockMvc mockMvc;

  @AfterEach
  void tearDown() {
    validatedCustomerService
        .getCustomers()
        .forEach(customer -> validatedCustomerService.deleteCustomer(customer.getId()));
  }

  @Test
  void createCustomerShouldCreateCustomerWithValidInput() throws Exception {
    final MvcResult result =
        mockMvc
            .perform(post("/customers").param("name", "name").param("surname", "surname"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("name"))
            .andExpect(jsonPath("$.surname").value("surname"))
            .andReturn();
    final String createdCustomerId =
        JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");

    final Optional<Customer> customer =
        validatedCustomerService.getCustomer(UUID.fromString(createdCustomerId));
    assertTrue(customer.isPresent());
    assertThat(customer.get().getName(), is("name"));
    assertThat(customer.get().getSurname(), is("surname"));
  }

  @Test
  void createCustomerShouldReturn422IfEmptyName() throws Exception {
    mockMvc
        .perform(post("/customers").param("name", "").param("surname", "surname"))
        .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
        .andExpect(jsonPath("$.error").value("Validation Error"))
        .andExpect(
            jsonPath("$.validationErrors[0]")
                .value("Expected string length in range [1, 200] but has length 0"));
  }

  @Test
  void createCustomerShouldReturn422IfEmptySurname() throws Exception {
    mockMvc
        .perform(post("/customers").param("name", "name").param("surname", ""))
        .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
        .andExpect(jsonPath("$.error").value("Validation Error"))
        .andExpect(
            jsonPath("$.validationErrors[0]")
                .value("Expected string length in range [1, 200] but has length 0"));
  }

  @Test
  void getCustomersShouldReturnCustomers() throws Exception {
    final Customer customer = validatedCustomerService.createCustomer("name", "surname");

    mockMvc
        .perform(get("/customers"))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$[0].id").value(customer.getId().toString()))
        .andExpect(jsonPath("$[0].name").value("name"))
        .andExpect(jsonPath("$[0].surname").value("surname"))
        .andReturn();
  }

  @Test
  void getCustomerShouldReturn404IfNotExists() throws Exception {
    mockMvc
        .perform(get("/customers/" + UUID.randomUUID()))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
  }

  @Test
  void getCustomerShouldReturnCustomerIfExists() throws Exception {
    final Customer customer = validatedCustomerService.createCustomer("name", "surname");

    mockMvc
        .perform(get("/customers/" + customer.getId()))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.id").value(customer.getId().toString()))
        .andExpect(jsonPath("$.name").value("name"))
        .andExpect(jsonPath("$.surname").value("surname"))
        .andReturn();
  }

  @Test
  void deleteCustomerShouldReturn404IfNotExists() throws Exception {
    mockMvc
        .perform(delete("/customers/" + UUID.randomUUID()))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
  }

  @Test
  void deleteCustomerShouldDeleteAndReturnCustomerIfExists() throws Exception {
    final Customer customer = validatedCustomerService.createCustomer("name", "surname");

    mockMvc
        .perform(delete("/customers/" + customer.getId()))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.id").value(customer.getId().toString()))
        .andExpect(jsonPath("$.name").value("name"))
        .andExpect(jsonPath("$.surname").value("surname"));

    final Optional<Customer> optional = validatedCustomerService.getCustomer(customer.getId());
    assertTrue(optional.isEmpty());
  }
}
