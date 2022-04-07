package com.electrolux.demo.status.store.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.Customer;
import com.electrolux.demo.status.store.models.HeartbeatLog;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class HeartBeatLogServiceTest {

  public static final String APPLIANCE_ID = "YS2R4X20005399401";

  @Autowired
  private ApplianceService applianceService;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private HeartBeatLogService heartBeatLogService;

  private Customer customer;

  @BeforeEach
  public void init() {
    customer = customerService.save(
        new Customer("Kalles Grustransporter AB", "Cementvägen 8, 111 11 Södertälje"));
    Appliance appliance = applianceService.save(
        new Appliance(customer, APPLIANCE_ID, "ABC123"));
  }

  @AfterEach
  public void destroy() {
    applianceService.delete(applianceService.getByApplianceId(APPLIANCE_ID).get());
    customerService.delete(customer);
  }

  @Test
  public void testHeartBeatLogCollectionOrderedByRecieveTimeAsc() {
    Appliance appliance = applianceService.getByApplianceId(APPLIANCE_ID).get();
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.parse("2022-04-07T11:30:00.00Z")));
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.parse("2022-04-07T11:29:00.00Z")));
    heartBeatLogService.save(new HeartbeatLog(appliance, Instant.parse("2022-04-07T11:20:00.00Z")));

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

    heartBeatLogService.getFirst25HeartBeats().forEach(hb -> heartBeatLogService.delete(hb));
    assertThat(heartBeatLogService.getFirst25HeartBeats()).isEmpty();
  }


}
