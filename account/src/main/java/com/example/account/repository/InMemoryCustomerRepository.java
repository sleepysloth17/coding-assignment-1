package com.example.account.repository;

import com.example.account.model.Customer;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InMemoryCustomerRepository
    extends CrudRepository<Customer, UUID>, CustomerRepository {}
