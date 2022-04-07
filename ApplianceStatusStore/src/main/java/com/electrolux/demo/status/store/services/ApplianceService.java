package com.electrolux.demo.status.store.services;

import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.repositories.ApplianceRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplianceService {

  private ApplianceRepository applianceRepository;

  @Autowired
  public ApplianceService(ApplianceRepository applianceRepository) {
    this.applianceRepository = applianceRepository;
  }

  @Transactional
  public Appliance save(Appliance appliance) {
    return applianceRepository.save(appliance);
  }

  @Transactional(readOnly = true)
  public Optional<Appliance> getByApplianceId(String applianceId) {
    return applianceRepository.findByApplianceId(applianceId);
  }

  @Transactional
  public void delete(Appliance appliance) {
    applianceRepository.delete(appliance);
  }

}