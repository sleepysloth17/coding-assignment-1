package com.example.account.service;

import com.example.account.dto.AccountDto;
import com.example.account.dto.CustomerDto;
import com.example.account.dto.TransactionDto;
import com.example.account.model.Account;
import com.example.account.model.Customer;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CustomerAssembler {

  private final ICustomerService customerService;

  private final IAccountService accountService;

  private final ITransactionService transactionService;

  public CustomerAssembler(
      ICustomerService customerService,
      IAccountService accountService,
      ITransactionService transactionService) {
    this.customerService = customerService;
    this.accountService = accountService;
    this.transactionService = transactionService;
  }

  public CustomerDto createCustomer(String name, String surname) {
    return customerService.createCustomer(name, surname);
  }

  public List<CustomerDto> getCustomers() {
    return customerService.getCustomers().stream()
        .map(
            customer ->
                new CustomerDto(
                    customer.getId(),
                    customer.getName(),
                    customer.getSurname(),
                    getAccountDtoList(customer)))
        .toList();
  }

  private List<AccountDto> getAccountDtoList(Customer customer) {
    return accountService.getCustomerAccounts(customer.getId()).stream()
        .map(this::getAccountDto)
        .toList();
  }

  private AccountDto getAccountDto(Account account) {
    final List<TransactionDto> transactions =
        transactionService.getTransactions(account.getId()).stream()
            .map(TransactionDto::fromTransaction)
            .toList();
    return AccountDto.fromAccount(account, transactions);
  }
}
