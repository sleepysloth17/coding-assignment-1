package com.example.account.repository;

import com.example.account.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

  Optional<Customer> findById(UUID id);

  void delete(Customer customer);

  Customer save(Customer customer);

  List<Customer> findAll();
}
