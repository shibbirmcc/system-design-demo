package com.electrolux.demo.status.store.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.Customer;
import java.util.Optional;
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
public class ApplianceRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ApplianceRepository applianceRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @BeforeEach
  public void addTestData() {
    Customer customer1 = entityManager.persist(
        new Customer("Kalles Grustransporter AB", "Cementvägen 8, 111 11 Södertälje"));
    Customer customer2 = entityManager.persist(
        new Customer("Johans Bulk AB", "Bulkvägen 12, 222 22 Stockholm"));
    Customer customer3 = entityManager.persist(
        new Customer("Haralds Värdetransporter AB", "Budgetvägen 1, 333 33 Uppsala"));
    entityManager.flush();

    entityManager.persist(new Appliance(customer1, "YS2R4X20005399401", "ABC123"));
    entityManager.persist(new Appliance(customer1, "VLUR4X20009093588", "DEF456"));
    entityManager.persist(new Appliance(customer1, "VLUR4X20009048066", "GHI789"));

    entityManager.persist(new Appliance(customer2, "YS2R4X20005388011", "JKL012"));
    entityManager.persist(new Appliance(customer2, "YS2R4X20005387949", "MN0345"));

    entityManager.persist(new Appliance(customer3, "YS2R4X20009048066", "PQR678"));
    entityManager.persist(new Appliance(customer3, "VLUR4X20005387055", "STU901"));
    entityManager.flush();
  }

  @AfterEach
  public void deleteTestData() {
    applianceRepository.deleteAll();
    customerRepository.deleteAll();
    entityManager.getEntityManager()
        .createNativeQuery("ALTER TABLE customers AUTO_INCREMENT = 1")
        .executeUpdate();
  }

  @Test
  public void testFindByApplianceId() {
    Optional<Appliance> appliance = applianceRepository.findByApplianceId("YS2R4X20005399401");
    assertThat(appliance).isNotEmpty();
    assertThat("ABC123").isEqualTo(appliance.get().getFactoryNr());
    assertThat(appliance.get().getCustomer()).isNotNull();
    assertThat(appliance.get().getCustomer().getId()).isEqualTo(1);
  }

}
