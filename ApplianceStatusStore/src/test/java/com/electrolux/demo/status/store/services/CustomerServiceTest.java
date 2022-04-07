package com.electrolux.demo.status.store.services;

import static org.assertj.core.api.Assertions.assertThat;

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
        new Customer("Kalles Grustransporter AB", "Cementvägen 8, 111 11 Södertälje"));

    Optional<Customer> customer1 = customerService.getById(customer.getId());
    assertThat(customer1).isNotEmpty();
    assertThat("Kalles Grustransporter AB").isEqualTo(customer1.get().getName());
    assertThat("Cementvägen 8, 111 11 Södertälje").isEqualTo(customer1.get().getAddress());

    customerService.delete(customer1.get());
    assertThat(customerService.getById(customer.getId())).isEmpty();
  }

}
