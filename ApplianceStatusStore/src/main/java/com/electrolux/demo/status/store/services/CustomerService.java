package com.electrolux.demo.status.store.services;

import com.electrolux.demo.status.store.models.Customer;
import com.electrolux.demo.status.store.repositories.CustomerRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Transactional(readOnly = true)
  public Optional<Customer> getById(Integer customerId) {
    return customerRepository.findById(customerId);
  }

  @Transactional
  public Customer save(Customer customer) {
    return customerRepository.save(customer);
  }

  @Transactional
  public void delete(Customer customer) {
    customerRepository.delete(customer);
  }
}
