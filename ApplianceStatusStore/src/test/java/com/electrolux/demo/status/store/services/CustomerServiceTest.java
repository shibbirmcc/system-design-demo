package com.electrolux.demo.status.store.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.electrolux.demo.status.store.TestDataConstants;
import com.electrolux.demo.status.store.models.Customer;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CustomerServiceTest {

  @Autowired
  private CustomerService customerService;

  @Test
  public void testGetById() {
    Customer customer = customerService.save(
        new Customer(TestDataConstants.CUSTOMER_NAME_1, TestDataConstants.CUSTOMER_ADDRESS_1));

    Optional<Customer> customer1 = customerService.getById(customer.getId());
    assertThat(customer1).isNotEmpty();
    assertThat(TestDataConstants.CUSTOMER_NAME_1).isEqualTo(customer1.get().getName());
    assertThat(TestDataConstants.CUSTOMER_ADDRESS_1).isEqualTo(customer1.get().getAddress());

    customerService.delete(customer1.get());
    assertThat(customerService.getById(customer.getId())).isEmpty();
  }

}
