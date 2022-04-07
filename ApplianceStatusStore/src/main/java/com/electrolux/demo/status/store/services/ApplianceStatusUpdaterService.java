package com.electrolux.demo.status.store.services;

import com.electrolux.demo.status.store.models.Appliance;
import com.electrolux.demo.status.store.models.HeartbeatLog;
import com.electrolux.demo.status.store.repositories.ApplianceRepository;
import com.electrolux.demo.status.store.repositories.HeartbeatLogRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ApplianceStatusUpdaterService {
  private HeartbeatLogRepository heartbeatLogRepository;
  private ApplianceRepository applianceRepository;

  @Autowired
  public ApplianceStatusUpdaterService(HeartbeatLogRepository heartbeatLogRepository, ApplianceRepository applianceRepository){
    this.heartbeatLogRepository = heartbeatLogRepository;
    this.applianceRepository = applianceRepository;
  }

  @Scheduled(fixedDelayString = "${statusupdater.fixedDelay.in.milliseconds}")
  public void updateApplianceConnectivityStatus(){
    List<HeartbeatLog> heartBeats = heartbeatLogRepository.findFirst25ByOrderByHeartbeatReceivedAt();
    heartBeats.forEach(heartBeat -> {
      Appliance appliance = heartBeat.getAppliance();
      appliance.setLastHeartBeatReceiveTime(heartBeat.getHeartbeatReceivedAt());
      applianceRepository.save(appliance);
      heartbeatLogRepository.delete(heartBeat);
    });
  }


}
