package com.example.account.dto;

import com.example.account.model.Account;
import com.example.account.model.Customer;
import com.example.account.service.AccountService;
import com.example.account.service.CustomerService;
import com.example.account.service.TransactionService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class Assembler {

  private final CustomerService customerService;

  private final AccountService accountService;

  private final TransactionService transactionService;

  public Assembler(
      CustomerService customerService,
      AccountService accountService,
      TransactionService transactionService) {
    this.customerService = customerService;
    this.accountService = accountService;
    this.transactionService = transactionService;
  }

  public Optional<CustomerDto> getCustomerDto(UUID customerId) {
    final Optional<Customer> customer = customerService.getCustomer(customerId);
    return customer.map(c -> CustomerDto.fromCustomer(c, getAccountDtoList(customerId)));
  }

  private List<AccountDto> getAccountDtoList(UUID customerId) {
    return accountService.getCustomerAccounts(customerId).stream()
        .map(this::getAccountDto)
        .toList();
  }

  private AccountDto getAccountDto(Account account) {
    final List<TransactionDto> transactions =
        transactionService.getAccountTransactions(account.getId()).stream()
            .map(TransactionDto::fromTransaction)
            .toList();
    return AccountDto.fromAccount(account, transactions);
  }
}
