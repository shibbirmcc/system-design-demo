package com.electrolux.demo.status.store.repositories;

import com.electrolux.demo.status.store.dto.ApplianceDetail;
import com.electrolux.demo.status.store.models.Appliance;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplianceRepository extends JpaRepository<Appliance, String> {

  Optional<Appliance> findByApplianceId(String applianceId);

  @Query("SELECT "
      + "new com.electrolux.demo.status.store.dto.ApplianceDetail"
      + "(a.customer.name, a.customer.address, a.applianceId, a.factoryNr, a.lastHeartBeatReceiveTime) "
      + "FROM Appliance a")
  Page<ApplianceDetail> getApplianceDetails(Pageable pageable);
}
