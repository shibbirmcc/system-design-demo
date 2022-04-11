package com.electrolux.demo.status.store.services;

import com.electrolux.demo.status.store.dto.ApplianceDetail;
import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.repositories.ApplianceRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Transactional(readOnly = true)
  public Page<ApplianceDetail> getApplianceDetails(Pageable page) {
    Page<ApplianceDetail> response = applianceRepository.getApplianceDetails(page);
    return response;
  }


  @Transactional
  public void delete(Appliance appliance) {
    applianceRepository.delete(appliance);
  }

}
