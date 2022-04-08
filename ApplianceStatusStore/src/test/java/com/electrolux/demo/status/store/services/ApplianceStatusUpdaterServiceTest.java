package com.electrolux.demo.status.store.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.electrolux.demo.status.store.TestDataConstants;
import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.Customer;
import com.electrolux.demo.status.store.models.HeartbeatLog;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        new Customer(TestDataConstants.CUSTOMER_NAME_1, TestDataConstants.CUSTOMER_ADDRESS_1));
    Appliance appliance = applianceService.save(
        new Appliance(customer, TestDataConstants.APPLIANCE_ID_1, TestDataConstants.FACTORY_NO_1));
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.parse("2022-04-07T11:30:00.00Z")));
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.parse("2022-04-07T12:30:00.00Z")));
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.parse("2022-04-07T13:30:00.00Z")));
  }

  @AfterEach
  public void destroy() {
    applianceService.delete(
        applianceService.getByApplianceId(TestDataConstants.APPLIANCE_ID_1).get());
    customerService.delete(customer);
  }


  @Test
  public void whenWaitThenSchedulerProcessesAndDeletesHeartBeatLogs() {
    List<HeartbeatLog> heartBeats = heartBeatLogService.getFirst25HeartBeats();
    assertNotNull(heartBeats);
    assertThat(heartBeats).isNotEmpty();
    assertThat(heartBeats).hasSize(3);

    List<HeartbeatLog> manuallySortedHeartBeats = new ArrayList<>(heartBeats);
    Comparator<HeartbeatLog> comp = (HeartbeatLog o1, HeartbeatLog o2) -> o1.getHeartbeatReceivedAt()
        .compareTo(o2.getHeartbeatReceivedAt());
    Collections.sort(manuallySortedHeartBeats, comp);
    assertThat(heartBeats).containsExactly(
        manuallySortedHeartBeats.toArray(new HeartbeatLog[heartBeats.size()]));

    Awaitility.await().atMost(Duration.ofSeconds(15))
        .untilAsserted(() -> verify(applianceStatusUpdaterService, atLeast(2)));
    assertThat(heartBeatLogService.getFirst25HeartBeats()).isEmpty();
    Optional<Appliance> appliance = applianceService.getByApplianceId(
        TestDataConstants.APPLIANCE_ID_1);
    assertThat(appliance).isNotEmpty();
    assertThat(Instant.parse("2022-04-07T13:30:00.00Z")).isEqualTo(
        appliance.get().getLastHeartBeatReceiveTime());
  }
}
