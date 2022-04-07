package com.electrolux.demo.status.store.repositories;

import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplianceRepository extends JpaRepository<Appliance, String> {
  List<Appliance> findByCustomerId(Integer customerId);
  
}
