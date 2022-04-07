package com.electrolux.demo.status.store.repositories;

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
public class HeartbeatLogRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ApplianceRepository applianceRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private HeartbeatLogRepository heartbeatLogRepository;

  private Appliance appliance1, appliance2, appliance3;

  @BeforeEach
  public void addTestData() {
    Customer customer1 = entityManager.persist(
        new Customer("Kalles Grustransporter AB", "Cementvägen 8, 111 11 Södertälje"));
    entityManager.flush();

    appliance1 = entityManager.persist(new Appliance(customer1, "YS2R4X20005399401", "ABC123"));
    appliance2 = entityManager.persist(new Appliance(customer1, "VLUR4X20009093588", "DEF456"));
    appliance3 = entityManager.persist(new Appliance(customer1, "VLUR4X20009048066", "GHI789"));
    entityManager.flush();
  }

  @AfterEach
  public void deleteTestDataIfExists() {
    heartbeatLogRepository.deleteAll();
    applianceRepository.deleteAll();
    customerRepository.deleteAll();
    entityManager.getEntityManager()
        .createNativeQuery("ALTER TABLE customers AUTO_INCREMENT = 1")
        .executeUpdate();
    appliance1 = appliance2 = appliance3 = null;
  }

  @Test
  public void testHeartBeatLogCollection() {
    entityManager.persist(new HeartbeatLog(appliance1, Instant.parse("2022-04-07T11:30:00.00Z")));
    entityManager.persist(new HeartbeatLog(appliance2, Instant.parse("2022-04-07T11:29:00.00Z")));
    entityManager.persist(new HeartbeatLog(appliance3, Instant.parse("2022-04-07T11:20:00.00Z")));
    entityManager.flush();

    List<HeartbeatLog> heartBeats = heartbeatLogRepository.findFirst25ByOrderByHeartbeatReceivedAt();
    assertNotNull(heartBeats);
    assertThat(heartBeats).isNotEmpty();
    assertThat(heartBeats).hasSize(3);

    List<HeartbeatLog> manuallySortedHeartBeats = new ArrayList<>(heartBeats);
    Comparator<HeartbeatLog> comp = (HeartbeatLog o1, HeartbeatLog o2) -> o1.getHeartbeatReceivedAt()
        .compareTo(o2.getHeartbeatReceivedAt());
    Collections.sort(manuallySortedHeartBeats, comp);
    assertThat(heartBeats).containsExactly(
        manuallySortedHeartBeats.toArray(new HeartbeatLog[heartBeats.size()]));
  }


}
