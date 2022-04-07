package com.electrolux.demo.status.store.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.Customer;
import com.electrolux.demo.status.store.models.HeartbeatLog;
import com.electrolux.demo.status.store.repositories.ApplianceRepository;
import com.electrolux.demo.status.store.repositories.CustomerRepository;
import com.electrolux.demo.status.store.repositories.HeartbeatLogRepository;
import java.time.Duration;
import java.time.Instant;
import javax.persistence.EntityManager;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ApplianceStatusUpdaterServiceTest {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private ApplianceRepository applianceRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private HeartbeatLogRepository heartbeatLogRepository;

  @SpyBean
  private ApplianceStatusUpdaterService applianceStatusUpdaterService;

  @BeforeEach
  public void addTestData() {
    Customer customer1 =
        customerRepository.save(
            new Customer("Kalles Grustransporter AB", "Cementvägen 8, 111 11 Södertälje"));

    Appliance appliance1 = applianceRepository.save(
        new Appliance(customer1, "YS2R4X20005399401", "ABC123"));
    Appliance appliance2 = applianceRepository.save(
        new Appliance(customer1, "VLUR4X20009093588", "DEF456"));
    Appliance appliance3 = applianceRepository.save(
        new Appliance(customer1, "VLUR4X20009048066", "GHI789"));

    heartbeatLogRepository.save(
        new HeartbeatLog(appliance1, Instant.parse("2022-04-07T11:30:00.00Z")));
    heartbeatLogRepository.save(
        new HeartbeatLog(appliance2, Instant.parse("2022-04-07T11:29:00.00Z")));
    heartbeatLogRepository.save(
        new HeartbeatLog(appliance3, Instant.parse("2022-04-07T11:20:00.00Z")));
  }

  @AfterEach
  public void deleteTestData() {
    heartbeatLogRepository.deleteAll();
    applianceRepository.deleteAll();
    customerRepository.deleteAll();
    entityManager.createNativeQuery("ALTER TABLE customers AUTO_INCREMENT = 1")
        .executeUpdate();
  }


  @Test
  public void whenWaitThenSchedulerProcessesAndDeletesHeartBeatLogs() {
    assertThat(heartbeatLogRepository.count()).isEqualTo(0);
    Awaitility.await().atMost(Duration.ofSeconds(15))
        .untilAsserted(() -> verify(applianceStatusUpdaterService, atLeast(2)));
    assertThat(heartbeatLogRepository.count()).isEqualTo(0);
  }
}
