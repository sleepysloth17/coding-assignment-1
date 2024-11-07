package com.example.account.dto;

import com.example.account.model.Customer;
import java.util.List;
import java.util.UUID;

public record CustomerDto(UUID id, String name, String surname, List<AccountDto> accounts) {
  public static CustomerDto fromCustomer(Customer customer, List<AccountDto> accounts) {
    return new CustomerDto(customer.getId(), customer.getName(), customer.getSurname(), accounts);
  }
}
