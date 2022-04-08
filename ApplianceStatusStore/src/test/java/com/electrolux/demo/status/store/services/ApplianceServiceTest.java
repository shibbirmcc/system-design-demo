package com.electrolux.demo.status.store.services;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ApplianceServiceTest {

  @Autowired
  private ApplianceService applianceService;

  @Autowired
  private CustomerService customerService;

  private Customer customer;
  private Appliance appliance1, appliance2, appliance3;

  @BeforeEach
  public void init() {
    customer = customerService.save(
        new Customer(TestDataConstants.CUSTOMER_NAME_1, TestDataConstants.CUSTOMER_ADDRESS_1));
    assertThat(customer).isNotNull();
    applianceService.save(
        new Appliance(customer, TestDataConstants.APPLIANCE_ID_1, TestDataConstants.FACTORY_NO_1));

    appliance1 =
        applianceService.save(
            new Appliance(customer, TestDataConstants.APPLIANCE_ID_1,
                TestDataConstants.FACTORY_NO_1,
                Instant.now()));
    appliance2 =
        applianceService.save(
            new Appliance(customer, TestDataConstants.APPLIANCE_ID_2,
                TestDataConstants.FACTORY_NO_2));
    appliance3 =
        applianceService.save(
            new Appliance(customer, TestDataConstants.APPLIANCE_ID_3,
                TestDataConstants.FACTORY_NO_3,
                Instant.now()));
  }

  @AfterEach
  public void destroy() {
    applianceService.delete(appliance1);
    applianceService.delete(appliance2);
    applianceService.delete(appliance3);
    customerService.delete(customer);
  }

  @Test
  public void testGetByApplianceId() {
    Optional<Appliance> appliance = applianceService.getByApplianceId(
        TestDataConstants.APPLIANCE_ID_1);
    assertThat(appliance).isNotEmpty();
    assertThat(TestDataConstants.FACTORY_NO_1).isEqualTo(appliance.get().getFactoryNr());
    assertThat(customer.getId()).isEqualTo(appliance.get().getCustomer().getId());

    applianceService.delete(appliance.get());
    assertThat(applianceService.getByApplianceId(TestDataConstants.APPLIANCE_ID_1)).isEmpty();
  }

  @Test
  public void testApplianceDetails() {
    Page<ApplianceDetail> applianceDetailsList = applianceService.getApplianceDetails(
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
