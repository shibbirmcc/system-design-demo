package com.electrolux.demo.status.store.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.electrolux.demo.status.store.TestDataConstants;
import com.electrolux.demo.status.store.dto.ApplianceDetail;
import com.electrolux.demo.status.store.dto.ApplianceStatus;
import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.Customer;
import java.time.Instant;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        new Customer(TestDataConstants.CUSTOMER_NAME_1, TestDataConstants.CUSTOMER_ADDRESS_1));
    Customer customer2 = entityManager.persist(
        new Customer(TestDataConstants.CUSTOMER_NAME_2, TestDataConstants.CUSTOMER_ADDRESS_2));
    Customer customer3 = entityManager.persist(
        new Customer(TestDataConstants.CUSTOMER_NAME_3, TestDataConstants.CUSTOMER_ADDRESS_3));
    entityManager.flush();

    entityManager.persist(
        new Appliance(customer1, TestDataConstants.APPLIANCE_ID_1, TestDataConstants.FACTORY_NO_1,
            Instant.now()));
    entityManager.persist(
        new Appliance(customer1, TestDataConstants.APPLIANCE_ID_2, TestDataConstants.FACTORY_NO_2));
    entityManager.persist(
        new Appliance(customer1, TestDataConstants.APPLIANCE_ID_3, TestDataConstants.FACTORY_NO_3,
            Instant.now()));

    entityManager.persist(
        new Appliance(customer2, TestDataConstants.APPLIANCE_ID_4, TestDataConstants.FACTORY_NO_4));
    entityManager.persist(
        new Appliance(customer2, TestDataConstants.APPLIANCE_ID_5, TestDataConstants.FACTORY_NO_5));

    entityManager.persist(
        new Appliance(customer3, TestDataConstants.APPLIANCE_ID_6, TestDataConstants.FACTORY_NO_6));
    entityManager.persist(
        new Appliance(customer3, TestDataConstants.APPLIANCE_ID_7, TestDataConstants.FACTORY_NO_7));
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
    Optional<Appliance> appliance = applianceRepository.findByApplianceId(
        TestDataConstants.APPLIANCE_ID_1);
    assertThat(appliance).isNotEmpty();
    assertThat(TestDataConstants.FACTORY_NO_1).isEqualTo(appliance.get().getFactoryNr());
    assertThat(appliance.get().getCustomer()).isNotNull();
    assertThat(appliance.get().getCustomer().getId()).isEqualTo(1);
  }

  @Test
  public void testApplianceDetailsCollection() {
    Page<ApplianceDetail> applianceDetailsList = applianceRepository.getApplianceDetails(
        Pageable.ofSize(25));
    applianceDetailsList.forEach(applianceDetail -> {
      if (TestDataConstants.APPLIANCE_ID_1.equals(applianceDetail.getApplianceId())
          || TestDataConstants.APPLIANCE_ID_3.equals(applianceDetail.getApplianceId())) {
        assertThat(ApplianceStatus.CONNECTED).isEqualTo(applianceDetail.getApplianceStatus());
      } else {
        assertThat(ApplianceStatus.DISCONNECTED).isEqualTo(applianceDetail.getApplianceStatus());
      }
    });
  }

}
