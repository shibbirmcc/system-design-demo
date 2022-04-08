package com.electrolux.demo.status.store.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.electrolux.demo.status.store.TestDataConstants;
import com.electrolux.demo.status.store.models.Customer;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CustomerRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CustomerRepository customerRepository;


  @BeforeEach
  public void addTestData() {
    entityManager.persist(
        new Customer(TestDataConstants.CUSTOMER_NAME_1, TestDataConstants.CUSTOMER_ADDRESS_1));
    entityManager.persist(
        new Customer(TestDataConstants.CUSTOMER_NAME_2, TestDataConstants.CUSTOMER_ADDRESS_2));
    entityManager.persist(
        new Customer(TestDataConstants.CUSTOMER_NAME_3, TestDataConstants.CUSTOMER_ADDRESS_3));
    entityManager.flush();
  }

  @AfterEach
  public void deleteTestData() {
    customerRepository.deleteAll();
    entityManager.getEntityManager()
        .createNativeQuery("ALTER TABLE customers AUTO_INCREMENT = 1")
        .executeUpdate();
  }

  @Test
  public void testGetById() {

    List<Customer> customers = customerRepository.findAll();
    assertThat(customers).isNotEmpty();
    assertThat(customers).hasSize(3);

    Customer customer1 = customers.get(0);
    assertNotNull(customer1);
    assertThat(TestDataConstants.CUSTOMER_NAME_1).isEqualTo(customer1.getName());
    assertThat(TestDataConstants.CUSTOMER_ADDRESS_1).isEqualTo(customer1.getAddress());

    Customer customer2 = customers.get(1);
    assertNotNull(customer2);
    assertThat(TestDataConstants.CUSTOMER_NAME_2).isEqualTo(customer2.getName());
    assertThat(TestDataConstants.CUSTOMER_ADDRESS_2).isEqualTo(customer2.getAddress());

    Customer customer3 = customers.get(2);
    assertNotNull(customer3);
    assertThat(TestDataConstants.CUSTOMER_NAME_3).isEqualTo(customer3.getName());
    assertThat(TestDataConstants.CUSTOMER_ADDRESS_3).isEqualTo(customer3.getAddress());
  }

}
