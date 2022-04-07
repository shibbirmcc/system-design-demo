package com.electrolux.demo.status.store.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.Customer;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ApplianceServiceTest {

  @Autowired
  private ApplianceService applianceService;

  @Autowired
  private CustomerService customerService;

  @Test
  public void testGetByApplianceId() {
    Customer customer = customerService.save(
        new Customer("Kalles Grustransporter AB", "Cementvägen 8, 111 11 Södertälje"));
    assertThat(customer).isNotNull();
    applianceService.save(new Appliance(customer, "YS2R4X20005399401", "ABC123"));

    Optional<Appliance> appliance = applianceService.getByApplianceId("YS2R4X20005399401");
    assertThat(appliance).isNotEmpty();
    assertThat("ABC123").isEqualTo(appliance.get().getFactoryNr());
    assertThat(customer.getId()).isEqualTo(appliance.get().getCustomer().getId());

    applianceService.delete(appliance.get());
    assertThat(applianceService.getByApplianceId("YS2R4X20005399401")).isEmpty();

    customerService.delete(customer);
    assertThat(customerService.getById(customer.getId())).isEmpty();
  }
}
