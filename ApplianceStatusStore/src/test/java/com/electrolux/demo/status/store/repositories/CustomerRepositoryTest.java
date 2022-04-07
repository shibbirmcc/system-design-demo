package com.electrolux.demo.status.store.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.electrolux.demo.status.store.models.Customer;
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
        new Customer("Kalles Grustransporter AB", "Cementvägen 8, 111 11 Södertälje"));
    entityManager.persist(new Customer("Johans Bulk AB", "Bulkvägen 12, 222 22 Stockholm"));
    entityManager.persist(
        new Customer("Haralds Värdetransporter AB", "Budgetvägen 1, 333 33 Uppsala"));
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
    Customer customer1 = customerRepository.getById(1);
    assertNotNull(customer1);
    assertThat("Kalles Grustransporter AB").isEqualTo(customer1.getName());
    assertThat("Cementvägen 8, 111 11 Södertälje").isEqualTo(customer1.getAddress());

    Customer customer2 = customerRepository.getById(2);
    assertNotNull(customer2);
    assertThat("Johans Bulk AB").isEqualTo(customer2.getName());
    assertThat("Bulkvägen 12, 222 22 Stockholm").isEqualTo(customer2.getAddress());

    Customer customer3 = customerRepository.getById(3);
    assertNotNull(customer3);
    assertThat("Haralds Värdetransporter AB").isEqualTo(customer3.getName());
    assertThat("Budgetvägen 1, 333 33 Uppsala").isEqualTo(customer3.getAddress());
  }

}
