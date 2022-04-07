package com.electrolux.demo.status.store.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.Customer;
import com.electrolux.demo.status.store.models.HeartbeatLog;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
  private ApplianceService applianceService;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private HeartBeatLogService heartBeatLogService;

  @SpyBean
  private ApplianceStatusUpdaterService applianceStatusUpdaterService;

  private Customer customer;

  @BeforeEach
  public void init() {
    customer = customerService.save(
        new Customer("Kalles Grustransporter AB", "Cementvägen 8, 111 11 Södertälje"));
    Appliance appliance = applianceService.save(
        new Appliance(customer, HeartBeatLogServiceTest.APPLIANCE_ID, "ABC123"));
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.parse("2022-04-07T11:30:00.00Z")));
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.parse("2022-04-07T12:30:00.00Z")));
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.parse("2022-04-07T13:30:00.00Z")));
  }

  @AfterEach
  public void destroy() {
    applianceService.delete(
        applianceService.getByApplianceId(HeartBeatLogServiceTest.APPLIANCE_ID).get());
    customerService.delete(customer);
  }


  @Test
  public void whenWaitThenSchedulerProcessesAndDeletesHeartBeatLogs() {
    List<HeartbeatLog> heartBeats = heartBeatLogService.getFirst25HeartBeats();
    assertNotNull(heartBeats);
    assertThat(heartBeats).isNotEmpty();
    assertThat(heartBeats).hasSize(3);
    Awaitility.await().atMost(Duration.ofSeconds(15))
        .untilAsserted(() -> verify(applianceStatusUpdaterService, atLeast(2)));
    assertThat(heartBeatLogService.getFirst25HeartBeats()).isEmpty();
    Optional<Appliance> appliance = applianceService.getByApplianceId(
        HeartBeatLogServiceTest.APPLIANCE_ID);
    assertThat(appliance).isNotEmpty();
    assertThat(Instant.parse("2022-04-07T13:30:00.00Z")).isEqualTo(
        appliance.get().getLastHeartBeatReceiveTime());
  }
}
